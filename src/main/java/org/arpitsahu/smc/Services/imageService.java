package org.arpitsahu.smc.Services;

import org.springframework.web.multipart.MultipartFile;


public interface imageService {

    public String uploadImage(MultipartFile contactImage, String image_id);

    public String geUrlFromPublicId(String publicId);
}
