package com.example.U5_W6_D5.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload di un'immagine su Cloudinary
     *
     * @param file Il file da caricare
     * @return L'URL dell'immagine caricata
     * @throws IOException Se si verifica un errore durante l'upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Il file è vuoto");
        }

        // Validazione del tipo di file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Il file deve essere un'immagine");
        }

        // Upload su Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "dipendenti",
                        "resource_type", "image"
                )
        );

        // Ritorna l'URL dell'immagine
        return (String) uploadResult.get("secure_url");
    }

    /**
     * Elimina un'immagine da Cloudinary
     *
     * @param imageUrl L'URL dell'immagine da eliminare
     * @throws IOException Se si verifica un errore durante l'eliminazione
     */
    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        // Estrai il public_id dall'URL
        String publicId = extractPublicIdFromUrl(imageUrl);

        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    /**
     * Estrae il public_id dall'URL di Cloudinary
     *
     * @param imageUrl L'URL dell'immagine
     * @return Il public_id
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                // Rimuovi la versione (v123456789/)
                path = path.replaceFirst("v\\d+/", "");
                // Rimuovi l'estensione
                int lastDot = path.lastIndexOf('.');
                if (lastDot > 0) {
                    path = path.substring(0, lastDot);
                }
                return path;
            }
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione del public_id: " + e.getMessage());
        }
        return null;
    }
}
