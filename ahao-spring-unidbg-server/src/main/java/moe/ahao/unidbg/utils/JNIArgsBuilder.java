package moe.ahao.unidbg.utils;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JNIArgsBuilder {
    private final VM vm;
    private final List<Object> args;

    public static JNIArgsBuilder create(VM vm) {
        JNIArgsBuilder builder = new JNIArgsBuilder(vm);
        return builder;
    }

    private JNIArgsBuilder(VM vm) {
        this.vm = vm;
        this.args = new ArrayList<>();
    }

    public JNIArgsBuilder addJniEnv() {
        // arg1 env
        this.args.add(vm.getJNIEnv());
        // arg2 jobject/jclazz 一般用不到，直接填0
        this.args.add(0);

        return this;
    }

    public JNIArgsBuilder addObject(DvmObject<?> object) {
        int offset = vm.addLocalObject(object);
        args.add(offset);

        return this;
    }

    public JNIArgsBuilder addString(String value) {
        StringObject object = new StringObject(vm, value);
        int offset = vm.addLocalObject(object);
        args.add(offset);

        return this;
    }

    public JNIArgsBuilder addBytes(String value) {
        return addBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    public JNIArgsBuilder addBytes(byte[] value) {
        ByteArray object = new ByteArray(vm, value);
        int offset = vm.addLocalObject(object);
        args.add(offset);

        return this;
    }

    public JNIArgsBuilder addBoolean(boolean value) {
        args.add(value ? 1 : 0);

        return this;
    }

    public Object[] build() {
        return args.toArray();
    }
}
