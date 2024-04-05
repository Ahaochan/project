package moe.ahao.unidbg.example;

// 导入通用且标准的类库

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.hook.hookzz.*;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.pointer.UnidbgPointer;
import com.github.unidbg.utils.Inspector;
import com.sun.jna.Pointer;
import keystone.Keystone;
import keystone.KeystoneArchitecture;
import keystone.KeystoneEncoded;
import keystone.KeystoneMode;
import moe.ahao.unidbg.utils.JNIArgsBuilder;

import java.io.File;
import java.util.Arrays;

public class sina extends AbstractJni{
    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;

    sina() {
        // 创建模拟器实例,进程名建议依照实际进程名填写，可以规避针对进程名的校验
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.sina.International").build();
        // 获取模拟器的内存操作接口
        final Memory memory = emulator.getMemory();
        // 设置系统类库解析
        memory.setLibraryResolver(new AndroidResolver(23));
        // 创建Android虚拟机,传入APK，Unidbg可以替我们做部分签名校验的工作
        vm = emulator.createDalvikVM(new File("D:\\Code\\Java\\project\\ahao-unidbg\\src\\main\\resources\\sinaInternational.apk"));
        //
//        vm = emulator.createDalvikVM(null);

        // 加载目标SO
        DalvikModule dm = vm.loadLibrary(new File("D:\\Code\\Java\\project\\ahao-unidbg\\src\\main\\resources\\libutility.so"), true); // 加载so到虚拟内存
        //获取本SO模块的句柄,后续需要用它
        module = dm.getModule();
        vm.setJni(this); // 设置JNI
        vm.setVerbose(true); // 打印日志
        // 样本连JNI OnLoad都没有
        // dm.callJNI_OnLoad(emulator); // 调用JNI OnLoad
    };

    public String calculateS(){
        DvmObject<?> context = vm.resolveClass("android/content/Context").newObject(null);// context

        Object[] args = JNIArgsBuilder.create(vm)
            .addJniEnv()
            .addObject(context)
            .addString("12345")
            .addString("r0ysue")
            .build();

        Number number = module.callFunction(emulator, 0x1E7C + 1, args);
        String result = vm.getObject(number.intValue()).getValue().toString();
        return result;
    };

    public void patchVerify(){
        int patchCode = 0x4FF00100; // mov r0,1 的机器码 https://armconverter.com/?code=mov%20r0,1
        emulator.getMemory().pointer(module.base + 0x1E86).setInt(0,patchCode);
    }

    public void patchVerify1(){
        Pointer pointer = UnidbgPointer.pointer(emulator, module.base + 0x1E86); // 找到native函数的地址
        assert pointer != null;
        byte[] code = pointer.getByteArray(0, 4);
        // 匹配BL sub_1C60机器码
        if (!Arrays.equals(code, new byte[]{ (byte)0xFF, (byte) 0xF7, (byte) 0xEB, (byte) 0xFE })) { // BL sub_1C60
            throw new IllegalStateException(Inspector.inspectString(code, "patch32 code=" + Arrays.toString(code)));
        }
        try (Keystone keystone = new Keystone(KeystoneArchitecture.Arm, KeystoneMode.ArmThumb)) {
            KeystoneEncoded encoded = keystone.assemble("mov r0,1");
            byte[] patch = encoded.getMachineCode();
            if (patch.length != code.length) { // path 和 原来的码长度要一致
                throw new IllegalStateException(Inspector.inspectString(patch, "patch32 length=" + patch.length));
            }
            pointer.write(0, patch, 0, patch.length);  // 直接填机器码
        }
    };

    public void HookMDStringold(){
        // 加载HookZz
        IHookZz hookZz = HookZz.getInstance(emulator);

        // 找到MDStringold方法
        hookZz.wrap(module.base + 0x1BD0 + 1, new WrapCallback<HookZzArm32RegisterContext>() { // inline wrap导出函数
            @Override
            public void preCall(Emulator<?> emulator, HookZzArm32RegisterContext ctx, HookEntryInfo info) {
                // 打印入参
                Pointer input = ctx.getPointerArg(0);
                System.out.println("input:" + input.getString(0));
            };

            @Override
            public void postCall(Emulator<?> emulator, HookZzArm32RegisterContext ctx, HookEntryInfo info) {
                // 打印出参
                Pointer result = ctx.getPointerArg(0);
                System.out.println("output:" + result.getString(0));
            }
        });
    }

    public static void main(String[] args) {
        sina test = new sina();
       // test.patchVerify();
        test.patchVerify1();
        test.HookMDStringold();
        System.out.println(test.calculateS());
    }
}
