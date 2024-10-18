package rs.edu.raf.rentinn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.responses.UserResponse;
import rs.edu.raf.rentinn.services.PropertyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/property")
@Tag(name = "Property", description = "Property API")
@CrossOrigin
@SecurityRequirement(name = "basicScheme")
@SecurityRequirement(name = "Authorization")
public class PropertyController {
    private final String uploadDir = "uploads/";

    private final PropertyService propertyService;


    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "add property", description = "adds a new property",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
//    @PreAuthorize("hasAuthority('readUser')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "multipart/form-data",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {Property.class}))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Property> createProperty(
            @RequestPart("property") Property property,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        System.out.println("USAO");

        List<String> imagePaths = new ArrayList<>();

        // Create a directory for each property using its ID (create a folder for new properties)
        String propertyDir = uploadDir + "property_" + property.getId();
        Path propertyDirPath = Paths.get(propertyDir);

        if (!Files.exists(propertyDirPath)) {
            Files.createDirectories(propertyDirPath);  // Create the directory if it doesn't exist
        }

        // Save each image with a generated UUID as the file name
        for (MultipartFile image : images) {
            String uniqueFilename = UUID.randomUUID() + ".jpg"; // Use a UUID for unique naming
            Path imagePath = propertyDirPath.resolve(uniqueFilename);
            Files.write(imagePath, image.getBytes());  // Save the image to the property folder
            imagePaths.add(imagePath.toString());  // Store the relative path
        }

        // Set the image paths in the Property entity
        property.setImagePaths(imagePaths);

        // Save the property with image paths
        Property savedProperty = propertyService.saveProperty(property);

        return ResponseEntity.ok(savedProperty);
    }
}
