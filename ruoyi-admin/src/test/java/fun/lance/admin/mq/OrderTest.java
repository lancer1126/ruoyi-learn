package fun.lance.admin.mq;

import fun.lance.admin.domain.entity.Subject;
import fun.lance.admin.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
public class OrderTest {

    @Autowired
    private SubjectService subjectService;

    @Test
    public void subjectTest() {
        Random random = new Random();
        int num = random.nextInt(10) + 1;

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < num; j++) {
                Subject subject = new Subject();
                subject.setSubjectNo("no-" + j);
                subject.setSubjectName("name-" + j);
                subject.setStudySiteId(12345L);
                subject.setCreateDate(new Date());
                subject.setUpdateDate(subject.getCreateDate());

                subjectService.addSubject(subject);
            }

            try {
                Thread.sleep(2000);
            } catch (Exception ignored) {
            }
        }
    }
}
