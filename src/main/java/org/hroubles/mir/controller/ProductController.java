package org.hroubles.mir.controller;

import org.hroubles.mir.controller.util.ControllerUtils;
import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.User;
import org.hroubles.mir.domain.enums.Tag;
import org.hroubles.mir.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String productList(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Product> page = "".equals(filter) ? productService.findAll(pageable): productService.search(filter, pageable);

        model.addAttribute("page", page);
        model.addAttribute("url", "/product");
        model.addAttribute("filter", UriUtils.encodePath(filter, "UTF-8"));
        return "productList";
    }

    @PostMapping
    public String productList(
            @AuthenticationPrincipal User user,
            @Valid Product product,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam("tag")Set<Tag> tags
    ) throws IOException {
        product.setSeller(user);
        product.setTags(tags);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
        } else {
            saveFile(product, file);
            productService.save(product);
        }

        return "redirect:/product";
    }

    @GetMapping("/{id}")
    public String getProduct(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("product", productService.findById(id).get());
        return "productPage";
    }

    private void saveFile(@Valid Product product, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (uploadDir.exists()) uploadDir.mkdir();

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            product.setFilename(resultFilename);
        }
    }

    @GetMapping("/add-product-page")
    public String addPage(Model model) {
        model.addAttribute("tags", Tag.values());
        return "add-product-page";
    }
}
