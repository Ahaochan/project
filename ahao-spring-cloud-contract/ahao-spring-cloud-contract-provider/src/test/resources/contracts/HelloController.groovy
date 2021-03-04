package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description '测试接口'
    request {
        method GET()
        url('/hello') {
            queryParameters {
                parameter("name", "ahao")
            }
        }
    }
    response {
        status OK()
        headers {
            contentType applicationJsonUtf8()
        }
        body("helloahao")
    }
}
