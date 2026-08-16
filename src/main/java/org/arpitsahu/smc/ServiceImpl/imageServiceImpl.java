package org.arpitsahu.smc.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.arpitsahu.smc.Helper.AppConstants;
import org.arpitsahu.smc.Services.imageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class imageServiceImpl implements imageService {

    //wew need to autowire cloudinary config ie App config
    //we will be doing this with the use of constructor injection

    //this way we injected cloudinary in our project so we can use it here
    public Cloudinary cloudinary;
    public imageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    //ye function uploads the image to cloudinary and returns the url of the image
    @Override
    public String uploadImage(MultipartFile contactImage, String image_id) {
        try {
            java.util.Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    contactImage.getBytes(),
                    ObjectUtils.asMap("public_id", image_id)
            );

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new IllegalStateException("Cloudinary did not return a secure URL for image: " + image_id);
            }

            return secureUrl.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not upload contact image to Cloudinary", e);
        }
    }

    @Override
    public String geUrlFromPublicId(String publicId) {
        return cloudinary.url().secure(true).transformation(
                new Transformation<>().width(AppConstants.Contact_Image_Width).height(AppConstants.Contact_Image_Height).crop(AppConstants.Contact_Image_Crop)
        ).generate(publicId);
    }


}
