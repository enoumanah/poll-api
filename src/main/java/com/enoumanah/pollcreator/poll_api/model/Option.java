package com.enoumanah.pollcreator.poll_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;

@Entity
@NoArgsConstructor
@Getter @Setter
@Table(indexes = {
        @Index(name = "idx_option_poll_id", columnList = "poll_id"),
        @Index(name = "idx_option_votes", columnList = "votes")
})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Long votes = 0L;

    @Version
    private Long version;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

}
