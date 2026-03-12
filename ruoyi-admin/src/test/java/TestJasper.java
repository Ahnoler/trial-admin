import com.ruoyi.RuoYiApplication;
import domain.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringJUnit4ClassRunner.class)
public class TestJasper {
    String username = System.getenv("USERNAME");

    // 导出模板
    @Test
    public void Test01() throws Exception {
        InputStream inputStream = TestJasper.class.getResourceAsStream("/jasper/test01.jasper");
        Map<String, Object> params = new HashMap<>();
        params.put("username", "李淼4");
        params.put("email", "1364245986@qq.com");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, "d:\\Users\\" + System.currentTimeMillis() + ".pdf");
    }

    // 中文支持
    @Test
    public void Test02() throws Exception {
        InputStream inputStream = TestJasper.class.getResourceAsStream("/jasper/test01.jasper");
        Map<String, Object> params = new HashMap<>();
        params.put("username", "李淼4");
        params.put("email", "1364245986@qq.com");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\" + username + "\\Downloads\\" + System.currentTimeMillis() + ".pdf");
    }

    // 图片处理
    @Test
    public void Test03() throws Exception {
        InputStream inputStream = TestJasper.class.getResourceAsStream("/jasper/img.jasper");
        Map<String, Object> params = new HashMap<>();
        InputStream localimg = Files.newInputStream(new File("C:\\Users\\water\\Pictures\\Screenshots\\770D21233596BFB90E96D27477B80DAC.jpg").toPath());
        params.put("localimg", localimg);
        URL url = new URL("http://172.20.100.198:8080/src/assets/images/logo-collapse.png");
        InputStream webimg = url.openStream();
        params.put("webimg", webimg);
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\" + username + "\\Downloads\\" + System.currentTimeMillis() + ".pdf");
    }

    // 导出数据库数据
    @Test
    public void Test04() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/jasper/db_test01.jasper");
        Map<String, Object> params = new HashMap<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/trial_dev", "root", "123456");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, connection);
        JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\" + username + "\\Downloads\\" + System.currentTimeMillis() + ".pdf");
    }

    // 绑定Java集合
    @Test
    public void Test05() throws Exception {
        InputStream inputStream = TestJasper.class.getResourceAsStream("/jasper/User.jasper");
        Map<String, Object> params = new HashMap<>();

        // 这边关于jdbc获取数据库记录的流程，可以由mybatis优化一下，使用mapper文件和service、impl之间的交互达成更加有可读性的效果
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "select user_id, dept_id, user_name, nick_name, email from sys_user";
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/trial_dev", "root", "123456");
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getLong("user_id"));
                user.setDept_id(resultSet.getLong("dept_id"));
                user.setUser_name(resultSet.getString("user_name"));
                user.setNick_name(resultSet.getString("nick_name"));
                user.setEmail(resultSet.getString("email"));
                users.add(user);
            }
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\" + username + "\\Downloads\\" + System.currentTimeMillis() + ".pdf");

        }
    }
}
