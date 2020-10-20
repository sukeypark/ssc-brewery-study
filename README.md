## Spring Security Authentication Process
1. 사용자가 요청을 전송하면 intercepter에 의해 Filter를 통과한다.
2. Security Filter를 통과하면서 사용자 인증정보는 `Authentication Manager`에 저장된다.
3. `Authentication Provider`는 `Authentication Manager`에 저장된 인증정보를 `User Details Service`와 `Password Encoder`를 이용하여 검증하고 결과를 Security Filter에 반환한다.
4. 인증이 유효하면 사용자의 정보는 `Security Context`에 저장된다.
5. 요청이 Servlet에 전달되어 처리된다.
