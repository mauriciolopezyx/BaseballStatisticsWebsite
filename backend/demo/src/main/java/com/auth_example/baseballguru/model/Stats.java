package com.auth_example.baseballguru.model;

import com.auth_example.baseballguru.dto.AddPlayerDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="batting_stats")
@Getter
@Setter
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable=false)
    private String player;
    @Column(nullable=false)
    private String pos;
    @Column(nullable=false)
    private int age;
    @Column(nullable=false)
    private String team;
    @Column(nullable=false)
    private String lg;
    @Column(nullable=false)
    private float war;
    @Column(nullable=false)
    private int g;
    @Column(nullable=false)
    private int pa;
    @Column(nullable=false)
    private int ab;
    @Column(nullable=false)
    private int r;
    @Column(nullable=false)
    private int h;
    @Column(nullable=false)
    private int dbl;
    @Column(nullable=false)
    private int tpl;
    @Column(nullable=false)
    private int hr;
    @Column(nullable=false)
    private int rbi;
    @Column(nullable=false)
    private int sb;
    @Column(nullable=false)
    private int cs;
    @Column(nullable=false)
    private int bb;
    @Column(nullable=false)
    private int so;
    @Column(nullable=false)
    private float ba;
    @Column(nullable=false)
    private float obp;
    @Column(nullable=false)
    private float slg;
    @Column(nullable=false)
    private float ops;
    @Column(nullable=false)
    private int tb;
    @Column(nullable=false)
    private int gidp;
    @Column(nullable=false)
    private int hbp;
    @Column(nullable=false)
    private int sh;
    @Column(nullable=false)
    private int sf;
    @Column(nullable=false)
    private int ibb;
    @Column(nullable=false)
    private String headshot;

    @Transient
    private boolean bookmarked;

    public Stats(AddPlayerDto addPlayerDto) {
        this.player = addPlayerDto.player();
        this.pos = addPlayerDto.pos();
        this.age = addPlayerDto.age();
        this.team = addPlayerDto.team();
        this.lg = addPlayerDto.lg();
        this.war = addPlayerDto.war();
        this.g = addPlayerDto.g();
        this.pa = addPlayerDto.pa();
        this.ab = addPlayerDto.ab();
        this.r = addPlayerDto.r();
        this.h = addPlayerDto.h();
        this.dbl = addPlayerDto.dbl();
        this.tpl = addPlayerDto.tpl();
        this.hr = addPlayerDto.hr();
        this.rbi = addPlayerDto.rbi();
        this.sb = addPlayerDto.sb();
        this.cs = addPlayerDto.cs();
        this.bb = addPlayerDto.bb();
        this.so = addPlayerDto.so();
        this.ba = addPlayerDto.ba();
        this.obp = addPlayerDto.obp();
        this.slg = addPlayerDto.slg();
        this.ops = addPlayerDto.ops();
        this.tb = addPlayerDto.tb();
        this.gidp = addPlayerDto.gidp();
        this.hbp = addPlayerDto.hbp();
        this.sh = addPlayerDto.sh();
        this.sf = addPlayerDto.sf();
        this.ibb = addPlayerDto.ibb();
        this.headshot = addPlayerDto.headshot();
    }

    public Stats() {}

}
