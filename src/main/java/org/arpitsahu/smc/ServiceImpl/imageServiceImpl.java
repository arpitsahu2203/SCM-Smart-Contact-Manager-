package org.arpitsahu.smc.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.arpitsahu.smc.Helper.AppConstants;
import org.arpitsahu.smc.Services.imageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", image_id
            ));

            return geUrlFromPublicId(image_id);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String geUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(
                new Transformation<>().width(AppConstants.Contact_Image_Width).height(AppConstants.Contact_Image_Height).crop(AppConstants.Contact_Image_Crop)
        ).generate(publicId);
    }


}
