package moe.ahao.unidbg.example;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.hook.hookzz.*;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.memory.MemoryBlock;
import com.github.unidbg.pointer.UnidbgPointer;
import com.github.unidbg.utils.Inspector;
import com.sun.jna.Pointer;
import moe.ahao.unidbg.utils.JNIArgsBuilder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class zuiyou extends AbstractJni{
    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;

    zuiyou() {
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.xiaochuankeji.tieba").build(); // 创建模拟器实例
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23)); // 设置系统类库解析
        vm = emulator.createDalvikVM(new File("D:\\Code\\Java\\project\\ahao-unidbg\\src\\main\\resources\\right573.apk")); // 创建Android虚拟机
        DalvikModule dm = vm.loadLibrary(new File("D:\\Code\\Java\\project\\ahao-unidbg\\src\\main\\resources\\libnet_crypto.so"), true); // 加载so到虚拟内存
        module = dm.getModule(); //获取本SO模块的句柄

        vm.setJni(this);
        vm.setVerbose(true);
        dm.callJNI_OnLoad(emulator);
    };

    public void native_init(){
        // 0x4a069
        Object[] args = JNIArgsBuilder.create(vm)
            .addJniEnv()
            .build();
        module.callFunction(emulator, 0x4a069, args);
    };

    private String callSign(){
        // 准备入参
        Object[] args = JNIArgsBuilder.create(vm)
            .addJniEnv()
            .addString("12345")
            .addBytes("r0ysue")
            .build();

        Number number = module.callFunction(emulator, 0x4a28D, args);
        return vm.getObject(number.intValue()).getValue().toString();
    };

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/izuiyou/common/base/BaseApplication->getAppContext()Landroid/content/Context;":
                return vm.resolveClass("android/content/Context").newObject(null);
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "android/content/Context->getClass()Ljava/lang/Class;":{
                return dvmObject.getObjectType();
            }
            case "java/lang/Class->getSimpleName()Ljava/lang/String;":{
                return new StringObject(vm, "AppController");
            }
            case "android/content/Context->getFilesDir()Ljava/io/File;":
            case "java/lang/String->getAbsolutePath()Ljava/lang/String;": {
                return new StringObject(vm, "/data/user/0/cn.xiaochuankeji.tieba/files");
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    };

    @Override
    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature){
            case "android/os/Debug->isDebuggerConnected()Z":{
                return false;
            }
        }
        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callStaticIntMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature){
            case "android/os/Process->myPid()I":{
                return emulator.getPid();
            }

        }
        throw new UnsupportedOperationException(signature);
    }

    public void hook65540(){
        // 加载HookZz
        IHookZz hookZz = HookZz.getInstance(emulator);

        hookZz.wrap(module.base + 0x65540 + 1, new WrapCallback<HookZzArm32RegisterContext>() { // inline wrap导出函数
            @Override
            // 类似于 frida onEnter
            public void preCall(Emulator<?> emulator, HookZzArm32RegisterContext ctx, HookEntryInfo info) {
                // 类似于Frida args[0]
                Inspector.inspect(ctx.getR0Pointer().getByteArray(0, 0x10), "Arg1");
                System.out.println(ctx.getR1Long());
                Inspector.inspect(ctx.getR2Pointer().getByteArray(0, 0x10), "Arg3");

                ctx.push(ctx.getR2Pointer());
            };

            @Override
            // 类似于 frida onLeave
            public void postCall(Emulator<?> emulator, HookZzArm32RegisterContext ctx, HookEntryInfo info) {
                Pointer output = ctx.pop();
                Inspector.inspect(output.getByteArray(0, 0x10), "Arg3 after function");
            }
        });
    }

    public void callMd5(){
        List<Object> list = new ArrayList<>(10);

        // arg1
        String input = "r0ysue";
        // malloc memory
        MemoryBlock memoryBlock1 = emulator.getMemory().malloc(16, false);
        // get memory pointer
        UnidbgPointer input_ptr=memoryBlock1.getPointer();
        // write plainText on it
        input_ptr.write(input.getBytes(StandardCharsets.UTF_8));

        // arg2
        int input_length = input.length();

        // arg3 -- buffer
        MemoryBlock memoryBlock2 = emulator.getMemory().malloc(16, false);
        UnidbgPointer output_buffer=memoryBlock2.getPointer();

        list.add(input_ptr);
        list.add(input_length);
        list.add(output_buffer);
        // run
        module.callFunction(emulator, 0x65540 + 1, list.toArray());

        // print arg3
        Inspector.inspect(output_buffer.getByteArray(0, 0x10), "output");
    };

    public static void main(String[] args) throws Exception {
        zuiyou test = new zuiyou();
       test.hook65540();
       test.native_init();
       System.out.println(test.callSign());
        test.callMd5();
    }
}
