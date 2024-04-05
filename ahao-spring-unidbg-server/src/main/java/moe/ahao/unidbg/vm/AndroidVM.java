package moe.ahao.unidbg.vm;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.EmulatorBuilder;
import com.github.unidbg.arm.backend.DynarmicFactory;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.virtualmodule.android.AndroidModule;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AndroidVM implements Closeable {
    protected final AndroidEmulator emulator;
    protected final VM vm;
    protected final Memory memory;
    protected final Map<String, DalvikModule> moduleMap = new HashMap<>();


    public AndroidVM(String packageName, boolean dynarmic, boolean unicorn) throws IOException {
        // 创建模拟器实例,进程名建议依照实际进程名填写，可以规避针对进程名的校验
        EmulatorBuilder<AndroidEmulator> builder = AndroidEmulatorBuilder.for64Bit().setProcessName(packageName);
        if (dynarmic) {
            builder.addBackendFactory(new DynarmicFactory(true));
        }
        if (unicorn) {
            builder.addBackendFactory(new Unicorn2Factory(true));
        }
        emulator = builder.build();
        emulator.getSyscallHandler().setVerbose(true);
        emulator.getSyscallHandler().setEnableThreadDispatcher(true);

        // 获取模拟器的内存操作接口
        memory = emulator.getMemory();
        // 设置系统类库解析
        memory.setLibraryResolver(new AndroidResolver(23));

        if (this.initApkFile() == null) {
            vm = emulator.createDalvikVM();
        } else {
            vm = emulator.createDalvikVM(this.initApkFile());
        }
        vm.setJni(this.initJni());
        vm.setVerbose(true);

        new AndroidModule(emulator, vm).register(memory);
    }

    public DalvikModule loadLibrary(String directoryPath, String soFilename, boolean forceCallInit) {
        DalvikModule dm = vm.loadLibrary(new File(directoryPath, soFilename), forceCallInit);
        dm.callJNI_OnLoad(emulator);

        moduleMap.put(soFilename, dm);

        // 打印日志很耗时
        // try {
        //     Module module = dm.getModule();
        //     String traceFile = soFilename.replace(".so", ".txt");
        //     PrintStream traceStream = new PrintStream(Files.newOutputStream(Paths.get(directoryPath, traceFile)), true);
        //     emulator.traceCode(module.base, module.base + module.size).setRedirect(traceStream);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        return dm;
    }

    public DvmClass findClass(String name, DvmClass... interfaces) {
        return vm.resolveClass(name, interfaces);
    }

    protected File initApkFile() {
        return null;
    }

    protected Jni initJni() {
        return new AbstractJni() {};
    };

    @Override
    public void close() throws IOException {
        emulator.close();
    }
}
