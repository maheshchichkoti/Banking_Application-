package com.example.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banking.dto.AccountInfo;
import com.example.banking.dto.BankResponse;
import com.example.banking.dto.UserRequest;
import com.example.banking.entity.User;
import com.example.banking.repository.UserRepository;
import com.example.banking.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	UserRepository userRepository;
	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/** 
		 *  Creating an account - saving a new user into the db 
		 *  check if user already has an account
		 */
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.build();
		User savedUser = userRepository.save(newUser);
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseInfo(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName()+ " " +savedUser.getLastName()+ " " +savedUser.getOtherName())
						.build())
				.build();
				
	}
}
