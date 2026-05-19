package game;

import static java.lang.System.exit;

public class excutable {


    public static void main (String[] args){
        games game = null;
        face f = new face();

        // 프로그램이 종료될 때까지 메뉴를 계속해서 띄우는 메인 루프 구조
        while (true) {
            int index = f.ask(); // 사용자의 최종 선택(게임 번호)을 받아옴

            if (index == 1) {
                game = new yatch();
            } else if (index == 2) {
                game = new blackjack();
            } else {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            if (game != null) {
                game.startGame();
            } else {
                System.out.println("게임 실행 실패.");
                System.exit(1);
            }
        }
    }
}