package moe.ahao.spring.cloud.openfeign.config;

import com.ahao.domain.entity.AjaxDTO;
import org.springframework.web.multipart.MultipartFile;

public class LocalhostFeignFallback implements LocalhostFeignApi {

    @Override
    public Integer path(Integer id) {
        return null;
    }

    @Override
    public AjaxDTO get1(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO get2(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO get3(AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO post1(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO post2(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO post3(AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO post4(String msg, AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart1(MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart2(MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart3(MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart4(AjaxDTO req, MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart5(AjaxDTO req, MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO multipart6(AjaxDTO req, MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO fallback() {
        return AjaxDTO.failure();
    }
}
