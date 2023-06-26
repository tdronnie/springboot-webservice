package com.seny.book.springboot.web;

import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.*;

public class ProfileControllerUnitTest {

    @Test
    public void real_profile이_호출된다() throws Exception {
        //given
        String expectedProfile = "real"; // real 활성화
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);

    }

    @Test
    public void  real_profile이_없으면_첫_번째가_조회된다() throws Exception {
        //given
        String expectedProfile = "oauth"; //oauth 활성화
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);

    }

    @Test
    public void  active_profile이_없으면_default가_조회된다() throws Exception {
        //given
        String excpectedProfile = "default";//활성화 된 것 없음
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(excpectedProfile);

    }

}