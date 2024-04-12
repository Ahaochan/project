package moe.ahao.unidbg.vm;

import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.StringObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CcVM extends AndroidVM {
    public static final String SO = "libte2.so";
    public static final String PACKAGE = new String(Base64.getDecoder().decode("Y29tLmNjYi5sb25namlMaWZl"), StandardCharsets.UTF_8);

    public CcVM() throws IOException {
        super(PACKAGE, false, true);

        // 加载目标SO
        this.loadLibrary(SO, true);
    }

    public String te2(String arg1, String arg2, byte[] arg3, String arg4) {
        String className = new String(Base64.getDecoder().decode("Y29tL2NjYi9jcnlwdG8vdHAvdG9vbC9UcFNhZmVVdGlscw=="), StandardCharsets.UTF_8);

        DvmClass clazz = vm.resolveClass(className);
        StringObject stringObject = clazz.callStaticJniMethodObject(emulator, "te2(Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;)Ljava/lang/String;", arg1, arg2, arg3, arg4);
        String result = stringObject.getValue();

        return result;
    }

    public String ad2(String arg1, String arg2, byte[] arg3, String arg4) {
        String className = new String(Base64.getDecoder().decode("Y29tL2NjYi9jcnlwdG8vdHAvdG9vbC9UcFNhZmVVdGlscw=="), StandardCharsets.UTF_8);

        DvmClass clazz = vm.resolveClass(className);
        StringObject stringObject = clazz.callStaticJniMethodObject(emulator, "ad2(Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;)Ljava/lang/String;", arg1, arg2, arg3, arg4);
        String result = stringObject.getValue();

        return result;
    }
}
