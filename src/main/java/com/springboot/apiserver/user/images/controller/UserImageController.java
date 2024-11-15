package com.springboot.apiserver.user.images.controller;

import com.springboot.apiserver.user.images.entity.UserImages;
import com.springboot.apiserver.user.images.entity.UserImagesRepository;
import com.springboot.apiserver.user.images.imagedto.UserImageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserImageController {

    private final UserImagesRepository userImagesRepository;

    @Autowired
    public UserImageController(UserImagesRepository userImagesRepository) {
        this.userImagesRepository = userImagesRepository;
    }

    @GetMapping("/get-images")
    public List<UserImageResponseDto> getUserImages(@RequestParam("USER_ID")int userId) {
        List<UserImages> userImagesList = userImagesRepository.findByUserId(userId);
        List<UserImageResponseDto> responseDtoList = new ArrayList<>();

        for (UserImages userImage : userImagesList) {
            UserImageResponseDto dto = new UserImageResponseDto(userImage.getUserImageId(), userImage.getUserImage()
            ,userImage.getSampleImage(),userImage.getPrompt(),userImage.getCreatedAt());
            responseDtoList.add(dto);
        }

        return responseDtoList;
    }
}
