package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description '测试接口'
    request {
        method GET()
        url('/hello') {
            queryParameters {
                parameter("name", value(consumer(regex('.*'))))
            }
        }
    }
    response {
        status OK()
        headers {
            contentType textPlain()
        }
        body("hello${fromRequest().query('name')}")
    }
}
