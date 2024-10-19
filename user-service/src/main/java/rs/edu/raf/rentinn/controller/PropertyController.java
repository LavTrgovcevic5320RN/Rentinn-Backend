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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.responses.UserResponse;
import rs.edu.raf.rentinn.services.BookingService;
import rs.edu.raf.rentinn.services.PropertyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    private final BookingService bookingService;


    @Autowired
    public PropertyController(PropertyService propertyService,
                              BookingService bookingService) {
        this.propertyService = propertyService;
        this.bookingService = bookingService;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all properties", description = "Get all properties")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = List.class,
                            subTypes = {Property.class}))}),
            @ApiResponse(responseCode = "403",
                    description = "You aren't authorized to get all cards by account number"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<PropertyDto>> getAvailableProperties(
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam int guests,
            @RequestParam int rooms) {

        List<PropertyDto> availableProperties = bookingService.findAvailableProperties(city, country, checkInDate, checkOutDate, guests, rooms);
        return ResponseEntity.ok(availableProperties);
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

        String propertyDir = uploadDir + "property_" + property.getId();
        Path propertyDirPath = Paths.get(propertyDir);

        if (!Files.exists(propertyDirPath)) {
            Files.createDirectories(propertyDirPath);
        }

        for (MultipartFile image : images) {
            String uniqueFilename = UUID.randomUUID() + ".jpg";
            Path imagePath = propertyDirPath.resolve(uniqueFilename);
            Files.write(imagePath, image.getBytes());
            imagePaths.add(imagePath.toString());
        }

        property.setImagePaths(imagePaths);

        Property savedProperty = propertyService.saveProperty(property);

        return ResponseEntity.ok(savedProperty);
    }
}
