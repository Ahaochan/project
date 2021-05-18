package moe.ahao.web.module.inventory.service;

import moe.ahao.web.module.inventory.request.Request;

public interface RequestAsyncProcessService {
    void process(Request request);
}
