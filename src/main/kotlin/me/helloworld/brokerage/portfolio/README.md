## Brokerage

계좌의 현금, 보유 주식, 미체결 주문과 실시간 시세를 조합해 포트폴리오 요약 제공

### 요청 흐름
1. 인증 및 식별: 클라이언트는 Authorization: Bearer <JWT> 형태로 요청합니다.
2. 헤더 필터링: `HeaderFilter`가 JWT 서명을 검증하고 사용자 식별자를 추출하여 내부 요청 헤더 X-USER-ID에 전파합니다.
3. 컨텍스트 주입: `UserContextResolver`가 헤더의 `X-USER-ID`를 `UserContext` 객체로 변환하여 컨트롤러 파라미터에 주입합니다.
4. 데이터 수집:
   - `AccountClient`를 통해 예수금을 조회합니다.
   - `OrderClient`를 통해 보유 주식 목록을 조회합니다.
   - 위에서 얻은 티커 리스트를 기반으로 `MarketPriceClient`에서 현재가를 한번에 불러옵니다.
   - 계산 및 반환: 수집된 데이터를 합산하여 총자산, 수익률 등이 포함된 `AccountSummaryResponse`를 반환합니다.

### 외부 서비스 연동
- 각 서비스별로 전용 `WebClient` 빈을 생성하여 관리합니다.
- 서비스 특성에 따라 Connect Timeout 및 Read Timeout을 차별화합니다.