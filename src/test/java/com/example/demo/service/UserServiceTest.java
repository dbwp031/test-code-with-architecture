package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase =  Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given
        String email = "kok202@naver.com";
        //when
        UserEntity result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }


    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        String email = "kok303@naver.com";
        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given
        //when
        UserEntity result = userService.getById(1L);
        //then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }


    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("kok202@kakao.")
                .address("Gyeongi")
                .nickname("kok202-k")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        //when
        UserEntity result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("Incheon")
                .nickname("kok202-n")
                .build();
        //when
        userService.update(1,  userUpdateDto);

        //then
        UserEntity userEntity = userService.getById(1L);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Incheon");
        assertThat(userEntity.getNickname()).isEqualTo("kok202-n");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된댜(){
        //given
        //when
        userService.login(1);

        //then
        UserEntity userEntity = userService.getById(1L);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        //given
        //when
        userService.verifyEmail(2, "aaaaaa-aaa-aaaaaaa");

        //then
        UserEntity userEntity = userService.getById(2L);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaa-aaa-aaaaaaa123");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
