package org.group4.dvdshopbackend.models.member.dto.getPageObject;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPageObjectRes<T> {

    private int page;
    private int size;
    private int total;

    private int navStart;
    private int navEnd;

    private boolean hasNext;
    private boolean hasPrev;

    private List<T> members;

    @Builder(builderMethodName = "withAll")
    public GetPageObjectRes(GetPageObjectReq request, List<T> members, int total) {

        if (0 > total)
            return;

        this.total = total;
        this.members = members;

        page = request.getPage();
        size = request.getSize();

        navEnd = (int)(Math.ceil((double) page / 10)) * 10;
        navStart = navEnd - 9;

        navEnd = navEnd > members.size() ? members.size() : navEnd;

        hasPrev = (navStart > 1);
        hasNext = (total > navEnd * size);

        request.getLink();

    }

}
