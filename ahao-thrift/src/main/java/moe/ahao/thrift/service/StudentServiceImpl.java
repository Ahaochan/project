package moe.ahao.thrift.service;

import moe.ahao.thrift.gen.Student;
import moe.ahao.thrift.gen.StudentNotFoundException;
import moe.ahao.thrift.gen.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService.Iface {
    private Map<Long, Student> memory = new HashMap<>();
    private long id = 0;

    @Override
    public String hello(String name) throws TException {
        return "hello, " + name +", you are " + memory.values().stream().anyMatch(s -> name.equals(s.getName()));
    }

    @Override
    public long save(Student student) throws TException {
        if(student.getId() == 0) {
            student.setId(++id);
            memory.put(student.getId(), student);
            return student.getId();
        } else {
            memory.put(student.getId(), student);
            return student.getId();
        }
    }

    @Override
    public Student get(long id) throws StudentNotFoundException {
        if(id == 0 || memory.get(id) == null) {
            throw new StudentNotFoundException("1", "学生没找到");
        }
        return memory.get(id);
    }

    @Override
    public List<Student> search(String name) throws TException {
        return memory.values()
            .stream().filter(s -> StringUtils.equals(s.getName(), name))
            .collect(Collectors.toList());
    }
}
