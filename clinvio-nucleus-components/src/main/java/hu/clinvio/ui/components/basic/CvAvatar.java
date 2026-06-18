package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvAvatar extends AbstractCvComponent {

    private String name;
    private String imageUrl;
    private String size = "md"; // xs, sm, md, lg, xl
    private String variant; // success, warning, danger, accent

    public static CvAvatar of(String name) {
        CvAvatar avatar = new CvAvatar();
        avatar.name = name;
        return avatar;
    }

    public CvAvatar imageUrl(String url) { this.imageUrl = url; return this; }
    public CvAvatar size(String size) { this.size = size; return this; }
    public CvAvatar variant(String variant) { this.variant = variant; return this; }

    @Override
    public String getComponentType() { return CvComponentType.AVATAR.getType(); }

    @Override
    public String getTemplate() {         return CvComponentType.AVATAR.getDefaultTemplate(); }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("name", name);
        model.put("imageUrl", imageUrl);
        model.put("size", size);
        model.put("variant", variant);
        model.put("initials", getInitials(name));
        return model;
    }

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, 1).toUpperCase();
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getSize() { return size; }
    public String getVariant() { return variant; }
}
