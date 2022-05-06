package moe.ahao.spring.boot.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {Starter.class, OrikaTest.OrikaStudentMapperFactoryConfigurer.class})
// @ContextConfiguration(classes = {OrikaAutoConfiguration.class, OrikaTest.OrikaStudentMapperFactoryConfigurer.class})
class OrikaTest {
    @Autowired
    private MapperFacade mapperFacade;

    @Test
    void simple() {
        List<Student> studentList = Student.getList("学生");
        Teacher teacher = Teacher.get();
        teacher.setStudentList(studentList);

        TeacherDTO teacherDTO = mapperFacade.map(teacher, TeacherDTO.class);
        List<Student> studentDTOList = teacherDTO.getStudentDTOList();

        Assertions.assertEquals(teacher.getName(), teacherDTO.getName());
        for (int i = 0; i < studentDTOList.size(); i++) {
            Assertions.assertEquals("学生" + i, studentDTOList.get(i).getName());
        }
    }

    public static class OrikaStudentMapperFactoryConfigurer implements OrikaMapperFactoryConfigurer {
        @Override
        public void configure(MapperFactory factory) {
            factory.classMap(Teacher.class, TeacherDTO.class)
                .field("studentList", "studentDTOList") // 双向映射
                // .fieldAToB("studentList", "studentDTOList") // 单向映射
                // .fieldBToA("studentList", "studentDTOList") // 单向映射
                // .exclude("name") // 移除指定字段的映射
                .byDefault()
                .register();
        }
    }

    public static class TeacherDTO {
        private String name;
        private List<Student> studentDTOList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Student> getStudentDTOList() {
            return studentDTOList;
        }

        public void setStudentDTOList(List<Student> studentDTOList) {
            this.studentDTOList = studentDTOList;
        }

    }

    public static class StudentDTO {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }

    public static class Teacher {
        private String name;
        private List<Student> studentList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Student> getStudentList() {
            return studentList;
        }

        public void setStudentList(List<Student> studentList) {
            this.studentList = studentList;
        }

        public static Teacher get() {
            Teacher teacher = new Teacher();
            teacher.name = "老师";
            return teacher;
        }


    }

    public static class Student {
        private String name;

        public Student() {
        }

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static List<Student> getList(String name) {
            return Stream.of(0, 1, 2, 3, 4, 5, 6, 7).map(i -> new Student(name + i)).collect(Collectors.toList());
        }
    }
}
