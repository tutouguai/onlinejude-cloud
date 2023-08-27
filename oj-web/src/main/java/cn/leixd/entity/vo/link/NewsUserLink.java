package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.News;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class NewsUserLink {
    private User user;
    private News news;

    public NewsUserLink(User user, News news) {
        this.user = user;
        this.news = news;
    }
}
