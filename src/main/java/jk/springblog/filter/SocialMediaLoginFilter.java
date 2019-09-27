package jk.springblog.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SocialMediaLoginFilter {

    private final AuthorizationCodeResourceDetails google;
    private final ResourceServerProperties googleResources;
    private final AuthorizationCodeResourceDetails facebook;
    private final ResourceServerProperties facebookResource;
    private final OAuth2ClientContext oAuth2ClientContext;



    public SocialMediaLoginFilter(AuthorizationCodeResourceDetails google,
                                  ResourceServerProperties googleResources,
                                  @Qualifier("oauth2ClientContext") OAuth2ClientContext oAuth2ClientContext,
                                  AuthorizationCodeResourceDetails facebook,
                                  ResourceServerProperties facebookResource)
                                   {
        this.google = google;
        this.googleResources = googleResources;
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.facebook = facebook;
        this.facebookResource = facebookResource;

    }

    public Filter authFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google, oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResources.getUserInfoUri(), google.getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        filters.add(googleFilter);


        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook, oAuth2ClientContext);
        facebookFilter.setRestTemplate(facebookTemplate);
        tokenServices = new UserInfoTokenServices(facebookResource.getUserInfoUri(), facebook.getClientId());
        tokenServices.setRestTemplate(facebookTemplate);
        facebookFilter.setTokenServices(tokenServices);
        filters.add(facebookFilter);

        filter.setFilters(filters);
        return filter;
    }
}
