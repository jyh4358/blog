package com.myblog.security.oauth2.service;

import com.myblog.security.oauth2.CustomOauth2User;
import com.myblog.security.oauth2.model.GoogleUser;
import com.myblog.security.oauth2.model.OAuth2UserInfo;
import com.myblog.member.model.Member;
import com.myblog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class Oauth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // todo - factory 사용으로 나중에 변경
        OAuth2UserInfo userInfo = new GoogleUser(oAuth2User.getAttributes());

        // todo - Oauth2Provider를 바로 넣는로직은 유지보수 관점에 안좋음 나중에 변경하자
        Member oauthMember = Member.createMember(
                oAuth2User.getAttribute("sub"),
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("picture"),
                userInfo.getOAuth2Provider());


        Member member = memberRepository.findByEmail(oAuth2User.getAttribute("email"))
                        .orElseGet(() -> memberRepository.save(oauthMember));

        changeMemberName(oAuth2User, member);

        return new CustomOauth2User(userInfo, member);

    }


    //동일 이름을 식별하기 위해 Google 식별키로 구분
    private void changeMemberName(OAuth2User oAuth2User, Member member) {
        String providerId = oAuth2User.getAttribute("sub");
        String subProviderId = providerId.substring(0, 5);
        String name = oAuth2User.getAttribute("name");
        member.setUsername(name + "#" + subProviderId);
    }

}
