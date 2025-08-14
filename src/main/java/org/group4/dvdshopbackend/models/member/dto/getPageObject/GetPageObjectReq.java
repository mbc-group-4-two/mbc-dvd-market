package org.group4.dvdshopbackend.models.member.dto.getPageObject;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPageObjectReq {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 20;

    private String type;

    private  String link;

    public String[] getTypes() {
        if (type == null || type.isEmpty())
            return null;
        return type.split(" ");
    }

    public Pageable getPageable(String... props) {

        int page = this.page - 1;
        int size = this.size;
        return PageRequest.of(page, size, Sort.by(props).ascending());

    }

    public String getLink() {

        StringBuilder sb = new StringBuilder();

        if (link == null) {
            sb.append("?page=");
            sb.append(this.page);

            sb.append("&size=");
            sb.append(this.size);

            String[] types = getTypes();
            if (types != null && types.length > 0) {
                sb.append("&types=");
                sb.append(type);
            }
            link = sb.toString();

        }
        return link;
    }

}
