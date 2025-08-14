package org.group4.dvdshopbackend.models.member.dto.getMemberList;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberListReq {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String scope;

    private String link;

    public String getLink() {

        if (link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (scope != null && scope.length() > 0) {

                builder.append("&scope=" + scope);

            }

            link = builder.toString();
        }
        return link;
    }   // 메서드 종료

    public String[] getScopes() {
        if (scope == null || scope.isEmpty()) {
            return null;
        }
        return scope.split(" ");
    }   // 메서드 종료

    public Pageable getPageable(String...sortColums) {
        if (sortColums.length > 0)
            return PageRequest.of(this.page-1, this.size, Sort.by(sortColums).descending());
        else
            return PageRequest.of(this.page-1, this.size);
    }   // 메서드 종료

}
