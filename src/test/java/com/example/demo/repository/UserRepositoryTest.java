package com.example.demo.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.model.UserStatus;

@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	//
	// @Test
	// void UserRepository_가_제대로_연결되었다() {
	// 	//given
	// 	UserEntity userEntity = new UserEntity();
	// 	userEntity.setEmail("kok202@naver.com");
	// 	userEntity.setAddress("Seoul");
	// 	userEntity.setNickname("kok202");
	// 	userEntity.setStatus(UserStatus.ACTIVE);
	// 	userEntity.setCertificationCode("aaaaaa-aaa-aaaaaaa");
	//
	// 	//when
	// 	UserEntity result = userRepository.save(userEntity);
	//
	// 	//then
	// 	assertThat(result.getId()).isNotNull();
	// }
	/*
	* 전체 테스트로 돌리면 테스트 실패
	* 혼자 돌리면 테스트 통과
	* => 비결정적이 테스트
	* => 테스트 매서드가 병렬적으로 처리되기 때문에 전체 테스트시 실패하게 된다.
	* */
	@Test
	void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
			// //given
			// UserEntity userEntity = new UserEntity();
			// userEntity.setEmail("kok202@naver.com");
			// userEntity.setId(1L);
			// userEntity.setAddress("Seoul");
			// userEntity.setNickname("kok202");
			// userEntity.setStatus(UserStatus.ACTIVE);
			// userEntity.setCertificationCode("aaaaaa-aaa-aaaaaaa");
			//
			// //when
  			// userRepository.save(userEntity);
			Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

			//then
			assertThat(result.isPresent()).isTrue();
	}

	@Test
	void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){
		// //given
		// UserEntity userEntity = new UserEntity();
		// userEntity.setEmail("kok202@naver.com");
		// userEntity.setId(1L);
		// userEntity.setAddress("Seoul");
		// userEntity.setNickname("kok202");
		// userEntity.setStatus(UserStatus.ACTIVE);
		// userEntity.setCertificationCode("aaaaaa-aaa-aaaaaaa");
		//
		// //when
		// userRepository.save(userEntity);
		Optional<UserEntity> result = userRepository.findByIdAndStatus(1,  UserStatus.PENDING);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
		// //given
		// UserEntity userEntity = new UserEntity();
		// userEntity.setEmail("kok202@naver.com");
		// userEntity.setId(1L);
		// userEntity.setAddress("Seoul");
		// userEntity.setNickname("kok202");
		// userEntity.setStatus(UserStatus.ACTIVE);
		// userEntity.setCertificationCode("aaaaaa-aaa-aaaaaaa");
		//
		// //when
		// userRepository.save(userEntity);
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("kok202@naver.com", UserStatus.ACTIVE);

		//then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){
		// //given
		// UserEntity userEntity = new UserEntity();
		// userEntity.setEmail("kok202@naver.com");
		// userEntity.setId(1L);
		// userEntity.setAddress("Seoul");
		// userEntity.setNickname("kok202");
		// userEntity.setStatus(UserStatus.ACTIVE);
		// userEntity.setCertificationCode("aaaaaa-aaa-aaaaaaa");
		//
		// //when
		// userRepository.save(userEntity);
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("dbwp031@naver.com", UserStatus.ACTIVE);

		//then
		assertThat(result.isEmpty()).isTrue();
	}
}
