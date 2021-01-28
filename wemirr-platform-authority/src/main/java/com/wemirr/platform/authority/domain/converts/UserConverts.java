package com.wemirr.platform.authority.domain.converts;

import com.wemirr.framework.boot.entity.BasePageConverts;
import com.wemirr.platform.authority.domain.dto.UserUpdateDTO;
import com.wemirr.platform.authority.domain.entity.User;
import com.wemirr.platform.authority.domain.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author Levin
 */
@Slf4j
public class UserConverts {


    public static final User2VoConverts USER_2_VO_CONVERTS = new User2VoConverts();
    public static final UserDto2PoConverts USER_DTO_2_PO_CONVERTS = new UserDto2PoConverts();

    public static class User2VoConverts implements BasePageConverts<User, UserVO> {

        @Override
        public UserVO convert(User source) {
            if (source == null) {
                return null;
            }
            UserVO target = new UserVO();
            BeanUtils.copyProperties(source, target);
            return target;
        }
    }

    public static class UserDto2PoConverts implements BasePageConverts<UserUpdateDTO, User> {

        @Override
        public User convert(UserUpdateDTO source) {
            if (source == null) {
                return null;
            }
            User target = new User();
            BeanUtils.copyProperties(source, target);
            return target;
        }
    }
}
