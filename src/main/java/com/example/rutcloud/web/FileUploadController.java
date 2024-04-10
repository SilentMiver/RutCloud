package com.example.rutcloud.web;

import com.example.rutcloud.config.exceptions.StorageFileNotFoundException;
import com.example.rutcloud.services.QRCodeGenerator;
import com.example.rutcloud.services.ShortUrlService;
import com.example.rutcloud.services.StorageService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.stream.Collectors;
@Deprecated

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final ShortUrlService shortUrlService;
    private final QRCodeGenerator qrCodeGenerator;

    @Autowired
    public FileUploadController(StorageService storageService, ShortUrlService shortUrlService, QRCodeGenerator qrCodeGenerator) {
        this.storageService = storageService;
        this.shortUrlService = shortUrlService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{id:.+}")
    public String getFileByShortUrl(@PathVariable String id){
        var value = shortUrlService.getByShortUrl(id);

        return "redirect:/files/" + value;
    }

    @GetMapping("/files/{filename:.+}/delete")
    public String deleteFile(@PathVariable String filename) throws IOException {
        storageService.deleteFileByName(filename);
        shortUrlService.deleteByUrl(filename);

        return "redirect:/";
    }
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Сохранение файла
            storageService.store(file);

            // Получение URL-адреса сохраненного файла
            String fileUrl = getFileUrl(file.getOriginalFilename());

            // Генерация QR-кода на основе URL-адреса файла
            byte[] qrCodeImage = qrCodeGenerator.generateQRCode(fileUrl);

            // Вставка QR-кода в модель для передачи на страницу
            model.addAttribute("qrCodeImage", qrCodeImage);

            return "redirect:/";
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            // Обработка ошибок генерации QR-кода или сохранения файла
            return "error";
        }
    }

    private String getFileUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
    }
    @GetMapping("/qr-code")
    public ResponseEntity<byte[]> showQRCode() {
        byte[] qrCodeImage;
        try {
            // Генерация QR-кода для страницы
            String uri = "shareddocuments://path/to/your/file/example.txt";
            qrCodeImage = qrCodeGenerator.generateQRCode(uri);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrCodeImage.length);

            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // Обработка ошибок генерации QR-кода
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
