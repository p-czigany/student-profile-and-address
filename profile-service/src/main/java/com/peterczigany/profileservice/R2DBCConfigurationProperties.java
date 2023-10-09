package com.peterczigany.profileservice;

import lombok.Getter;
import lombok.Setter;
// import javax.validation.constraints;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "r2dbc")
public class R2DBCConfigurationProperties {

  //    @NotEmpty
  private String url;
  private String user;
  private String password;

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @param user the user to set
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
}
