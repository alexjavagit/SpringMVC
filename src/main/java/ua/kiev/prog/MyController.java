package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, byte[]> photos = new HashMap<Long, byte[]>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping("/list")
    public String onList(Model model) {
        model.addAttribute("pictures", photos);
        return "list";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {

        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    @RequestMapping(value = "/delete_pics", method = RequestMethod.POST)
    public String onDelete(@RequestParam(value = "del[]", required = false) long[] toDelete) {
        if (toDelete == null || toDelete.length == 0)
            throw new PhotoNotFoundException();
        else {
            for (long id : toDelete) {
                photos.remove(id);
            }
            return "redirect: /list";
        }
    }

    @RequestMapping(value="/achieve_pics", method = RequestMethod.POST)
    private String onAchieve(@RequestParam(value = "del[]", required = false) long[] toAchieve) throws IOException {
        if (toAchieve == null || toAchieve.length == 0)
            throw new PhotoNotFoundException();
        else {
            File f = new File("pics.zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(f));
            for (Long id : toAchieve) {
                ZipEntry e = new ZipEntry(id+"jpg");
                zipOutputStream.putNextEntry(e);
                byte[] data = photos.get(id);
                zipOutputStream.write(data, 0, data.length);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
        }
        return "redirect: /";
    }

    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

}
