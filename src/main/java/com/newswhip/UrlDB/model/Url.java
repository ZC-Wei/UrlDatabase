package com.newswhip.UrlDB.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "url")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @GeneratedValue
    private Integer id;
    private String domain;
    private String url;
    private Integer socialScore;
}
