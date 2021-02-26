namespace java moe.ahao.thrift.gen

/**
* 学生类
**/
enum SEX {
	MALE,
	FEMALE,
	UNKONWN
}

struct Student {
    1: i64 id;
    2: string name;
    3: i8 age;
    4: SEX sex;
}

exception StudentNotFoundException {
	1: string code;
	2: string message;
}

service StudentService {
    string hello(string name);

    i64 save(1:Student student);
    Student get(1:i64 id) throws (StudentNotFoundException e);
    list<Student> search(1:string name);
}
