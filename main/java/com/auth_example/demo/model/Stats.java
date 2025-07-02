package com.auth_example.demo.model;

import com.auth_example.demo.dto.AddPlayerDto;
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

    public Stats(AddPlayerDto addPlayerDto) {
        this.player = addPlayerDto.getPlayer();
        this.pos = addPlayerDto.getPos();
        this.age = addPlayerDto.getAge();
        this.team = addPlayerDto.getTeam();
        this.lg = addPlayerDto.getLg();
        this.war = addPlayerDto.getWAR();
        this.g = addPlayerDto.getG();
        this.pa = addPlayerDto.getPA();
        this.ab = addPlayerDto.getAB();
        this.r = addPlayerDto.getR();
        this.h = addPlayerDto.getH();
        this.dbl = addPlayerDto.getDBL();
        this.tpl = addPlayerDto.getTPL();
        this.hr = addPlayerDto.getHR();
        this.rbi = addPlayerDto.getRBI();
        this.sb = addPlayerDto.getSB();
        this.cs = addPlayerDto.getCS();
        this.bb = addPlayerDto.getBB();
        this.so = addPlayerDto.getSO();
        this.ba = addPlayerDto.getBA();
        this.obp = addPlayerDto.getOBP();
        this.slg = addPlayerDto.getSLG();
        this.ops = addPlayerDto.getOPS();
        this.tb = addPlayerDto.getTB();
        this.gidp = addPlayerDto.getGIDP();
        this.hbp = addPlayerDto.getHBP();
        this.sh = addPlayerDto.getSH();
        this.sf = addPlayerDto.getSF();
        this.ibb = addPlayerDto.getIBB();
        this.headshot = addPlayerDto.getHeadshot();
    }

    public Stats() {}

}
