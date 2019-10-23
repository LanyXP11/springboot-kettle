package com.lx.kettle.web.service.impl;

        import com.lx.kettle.common.tootik.Constant;
        import com.lx.kettle.common.utils.MD5Utils;
        import com.lx.kettle.core.dto.BootTablePage;
        import com.lx.kettle.core.mapper.KUserDao;
        import com.lx.kettle.core.model.KUser;
        import com.lx.kettle.web.service.UserService;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.Date;
        import java.util.List;

/**
 * Created by chenjiang on 2019/10/21
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {
    @Autowired
    private KUserDao kuserDao;

    /**
     * 用户登录
     *
     * @param kUser
     * @return
     */
    @Override
    public KUser login(KUser kUser) {
        KUser templateOne = KUser.builder().uAccount(kUser.getuAccount()).delFlag(1).build();
        KUser uu = this.kuserDao.templateOne(templateOne);
        String inputPassward = MD5Utils.Encrypt(kUser.getuPassword(), true);
        if (uu != null && uu.getuPassword().equals(inputPassward)) {
            return uu;
        }
        return null;
    }

    /**
     * 判断是不是管理员
     *
     * @param uid
     * @return
     */
    @Override
    public Boolean isAdmin(Integer uid) {
        try {
            KUser user = kuserDao.unique(uid);
            if (user.getuAccount().equalsIgnoreCase(Constant.ADMIN)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("[根据ID查询用户查询不到用户uid={},essage:{}", uid, e);
            return false;
        }
    }

    /**
     * 查询用户列表
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public Object getListByPage(Integer starts, Integer size) {
        Integer start = 1;
        if (starts == 0) {
            //TODO 分页查询有BUG 项目中没有集成分页
        }
        List<KUser> resultUserList = this.kuserDao.template(KUser.builder().delFlag(1).build(), start, size);
        long countUsers = kuserDao.templateCount(KUser.builder().delFlag(1).build());
        return BootTablePage.builder().rows(resultUserList).total(countUsers).build();
    }

    /**
     * 添加用户
     *
     * @param insertUser
     * @param integer
     */
    @Override
    public void insert(KUser insertUser, Integer editUserId) {
        this.kuserDao.insert(KUser.builder()
                .addTime(new Date())
                .delFlag(1)
                .uAccount(MD5Utils.Encrypt(insertUser.getuPassword(),true))
                .editTime(new Date())
                .editUser(editUserId)
                .uNickname(insertUser.getuNickname())
                .uEmail(insertUser.getuEmail()).uPhone(insertUser.getuPhone())
                .build());
    }

}
