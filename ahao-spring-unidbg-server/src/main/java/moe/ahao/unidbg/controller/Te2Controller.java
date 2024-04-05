package moe.ahao.unidbg.controller;

import moe.ahao.unidbg.vm.Te2VM;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.CodecHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class Te2Controller {
    @PostMapping("/te2")
    public String te2(@RequestBody Object body) throws Exception {
        String arg1 = CodecHelper.parseBase64("MjRmZjdhMjU2ZDllY2Q3MTkyN2E1NTlhM2U4ZWQwYWJjZTQ0OTQ3ZmQ3YjA0ZTA3MDNhOTkzMDFiZmE2OWZhNjM4NjkwYzExZjYwZWMyZjBiMDcyYjA1MDBjMjhkYWEzY2RkM2U5MmNkZjViMjdhZjA0Y2MyYjg2NGM5NTYzMTgzMzdlYjg3ZTU4ZDQwMGQwMjljMTFlNmExNzcwMzU0NGMyZWYzZGUzMTBiYzFjNGRiYTU2ODU2MmEwOGY1NTUwZDcwYWQyYTMxYjViNTJhNjU1MjNiYjIzNDI0OTgxMWIxZjE3M2I2ZjU5NjY0NGViODgyZDM3MWY0MzRmYmY3OTllZGE0OTEwOTA5OTY5OTMwM2Q1YmE2ZTlhZWQwMWZjZWY2ZTc3OWFlZWRkMTZiMjE2MDA4NjI1MDg2N2IzYzRkMDAyNzA5NTQ5YzExNGIyNTFmYjk3MGIzZmJjMzkwYTlmODJkMDQ5");
        String arg2 = CodecHelper.parseBase64("K2VnQTl5MlVGZU1sUnRVR29HNXFRNXR3ZGFjbkFub2hQc21oSjlXK1ptQT0=");
        byte[] arg3 = CodecHelper.parseBase64("cWUzY2E4emU=").getBytes(StandardCharsets.UTF_8);
        String arg4 = JSONHelper.toString(body);

        try (Te2VM test = new Te2VM()) {
            String result = test.te2(arg1, arg2, arg3, arg4);
            return result;
        }
    }
}
