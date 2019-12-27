package com.ge.travle.service.impl;

import com.ge.travle.dao.UserDao;
import com.ge.travle.dao.impl.UserDaoImpl;
import com.ge.travle.domain.User;
import com.ge.travle.service.UserService;
import com.ge.travle.util.MailUtils;
import com.ge.travle.util.UuidUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean register(User user) {


        User u = userDao.findByUsername(user.getUsername());
        if (!StringUtils.isEmpty(u)) {
            return false;
        }

        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        try {
            System.out.println(BeanUtils.describe(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDao.save(user);

        String content = "<a href='http://uepww8.natappfree.cc/activeUserServlet?code=" + user.getCode() + "'>点击激活【旅游网】</a>";

        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;
    }

    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
