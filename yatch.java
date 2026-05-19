package game;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
//bot 을 조만간 더 잘구현하겠습니다..

public class yatch extends games{
    SecureRandom rand = new SecureRandom();
    Scanner input = new Scanner(System.in);

    int[] your_score = new int[12];
    int[] bot_score = new int[12]; //이거 써야되는디.. < 썻음 근데 좀 얘가 많이 멍청함
    boolean[] is_your_score_empty = new boolean[12];
    boolean[] is_bot_score_empty = new boolean[12];
    int[] current_dice = new int[5];

    //1,2,3,4,5,6 / choice / four of a kind, fullhouse, little straight, big straight, yacht
    //총 12개.

    public void roll_dice() {

        for (int i = 0; i < 5; i++) {
            current_dice[i] = 1 + rand.nextInt(6);
        }

    }

    public boolean[] which_to_reroll() {
        String input_;
        boolean[] buf = new boolean[5];

        //parsing 해서 숫자와 ',' 가 같이 들어오면 ,와 공백무시,숫자만. charat 으로.
        System.out.println("다시 굴리고 싶은 주사위의 순서를 입력해주세요.");
        System.out.println("아직 예외처리를 하지 않아서.. 잘 입력해 주셔야 합니다.");
        System.out.println("바꾸고 싶으신게 없다면 입력하지 않으셔도 됩니다.");
        System.out.println("ex ) 1,3,5 ");

        System.out.print("입력 : ");
        input_ = input.nextLine();

        //파싱..?
        for (int i = 0; i < input_.length(); i++) {
            char c = input_.charAt(i);
            if (c >= '1' && c <= '5') {
                int index = c - '1'; // '1'이면 0번 인덱스, '3'이면 2번 인덱숭
                buf[index] = true;
            }
        }

        return buf;
    }

    public void reroll(boolean[] see) {
        for (int i = 0; i < 5; i++) {
            if (see[i]) current_dice[i] = 1 + rand.nextInt(6);
        }
    }
    // -------------------------------------------------------------------------------------

    public void enroll(int where) {
        //1,2,3,4,5,6, choice , 4of kind, full house, str8 * 2 ,yatch
        int temp = 0;
        int[] sorted_dice = current_dice.clone(); //흠,, 원본유지
        Arrays.sort(sorted_dice);

        if (!is_your_score_empty[where - 1]) {
            System.out.println("이미 점수를 등록한 칸입니다!");
            return;
        }

        if (where < 7) {
            for (int i = 0; i < 5; i++) {
                if (where == current_dice[i]) temp = temp + where;
            }
            your_score[where - 1] = temp;
            is_your_score_empty[where - 1] = false;
            return;
        }

        switch (where) {
            case 7:
                for (int i = 0; i < 5; i++) temp = temp + current_dice[i];
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;
            case 8:
                if (sorted_dice[0] == sorted_dice[3] || sorted_dice[1] == sorted_dice[4]) {
                    for (int i = 0; i < 5; i++) {
                        temp = temp + current_dice[i];
                    }
                }
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;

            case 9:
                boolean case1 = (sorted_dice[0] == sorted_dice[2]) && (sorted_dice[3] == sorted_dice[4]);
                boolean case2 = (sorted_dice[0] == sorted_dice[1]) && (sorted_dice[2] == sorted_dice[4]);

                if (case1 || case2) {
                    for (int i = 0; i < 5; i++) {
                        temp = temp + current_dice[i];
                    }
                }
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;
            case 10:
                // 레전드로 우아한 bitwise 연산 하나 끓여오기, 다시 봐도 이쁨.. 시스템프로그래밍 체고
                int bitmask = 0;
                for (int i = 0; i < 5; i++) {
                    bitmask |= (1 << current_dice[i]);
                }
                if ((bitmask & 30) == 30 || (bitmask & 60) == 60 || (bitmask & 120) == 120)
                    temp = 15;
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;
            case 11:
                temp = 30;
                for (int i = 0; i < 4; i++) {
                    if (sorted_dice[i + 1] - sorted_dice[i] != 1) temp = 0;
                }
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;
            case 12:
                temp = 50;
                for (int i = 0; i < 4; i++) {
                    if (sorted_dice[i + 1] != sorted_dice[i]) temp = 0;
                }
                your_score[where - 1] = temp;
                is_your_score_empty[where - 1] = false;
                break;
        }
    }

    public int get_subtotal(int[] score, boolean[] is_empty) {
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            if (!is_empty[i]) sum += score[i];
        }
        return sum; //이게 히든 점수
    }

    public void enroll_cacluated(int where) {
        your_score[where - 1] = calculate_score(where);
        is_your_score_empty[where - 1] = false;
    }

    // 어디에 넣으면 몇 점이 나오는지 '계산만' 해서 리턴해주는 헬퍼 메서드 (유저/봇 공용)
    public int calculate_score(int where) {
        int temp = 0;
        int[] sorted_dice = current_dice.clone();
        Arrays.sort(sorted_dice);

        if (where < 7) {
            for (int i = 0; i < 5; i++) {
                if (where == current_dice[i]) temp = temp + where;
            }
            return temp;
        }

        switch (where) {
            case 7: // Choice
                for (int i = 0; i < 5; i++) temp = temp + current_dice[i];
                break;
            case 8: // 4 of kind
                if (sorted_dice[0] == sorted_dice[3] || sorted_dice[1] == sorted_dice[4]) {
                    for (int i = 0; i < 5; i++) temp = temp + current_dice[i];
                }
                break;
            case 9: // Full house
                boolean case1 = (sorted_dice[0] == sorted_dice[2]) && (sorted_dice[3] == sorted_dice[4]);
                boolean case2 = (sorted_dice[0] == sorted_dice[1]) && (sorted_dice[2] == sorted_dice[4]);
                if (case1 || case2) {
                    for (int i = 0; i < 5; i++) temp = temp + current_dice[i];
                }
                break;
            case 10: // Small Straight
                int bitmask = 0;
                for (int i = 0; i < 5; i++) bitmask |= (1 << current_dice[i]);
                if ((bitmask & 30) == 30 || (bitmask & 60) == 60 || (bitmask & 120) == 120) temp = 15;
                break;
            case 11: // Big Straight
                temp = 30;
                for (int i = 0; i < 4; i++) {
                    if (sorted_dice[i + 1] - sorted_dice[i] != 1) temp = 0;
                }
                break;
            case 12: // Yacht
                temp = 50;
                for (int i = 0; i < 4; i++) {
                    if (sorted_dice[i + 1] != sorted_dice[i]) temp = 0;
                }
                break;
        }
        return temp;
    }

    public void scoreboard() {
        System.out.println("\n===== YOUR SCOREBOARD =====");

        // 점수가 비어있으면 "-", 채워져 있으면 점수를 숫자로 출력
        System.out.printf(" 1          : %s\n", is_your_score_empty[0] ? "-" : your_score[0]);
        System.out.printf(" 2          : %s\n", is_your_score_empty[1] ? "-" : your_score[1]);
        System.out.printf(" 3          : %s\n", is_your_score_empty[2] ? "-" : your_score[2]);
        System.out.printf(" 4          : %s\n", is_your_score_empty[3] ? "-" : your_score[3]);
        System.out.printf(" 5          : %s\n", is_your_score_empty[4] ? "-" : your_score[4]);
        System.out.printf(" 6          : %s\n", is_your_score_empty[5] ? "-" : your_score[5]);
        System.out.printf(" choice     : %s\n", is_your_score_empty[6] ? "-" : your_score[6]);
        System.out.printf(" 4 of kind  : %s\n", is_your_score_empty[7] ? "-" : your_score[7]);
        System.out.printf(" Full house : %s\n", is_your_score_empty[8] ? "-" : your_score[8]);
        System.out.printf(" Small Str8 : %s\n", is_your_score_empty[9] ? "-" : your_score[9]);
        System.out.printf(" Big Str8   : %s\n", is_your_score_empty[10] ? "-" : your_score[10]);
        System.out.printf(" yacht      : %s\n", is_your_score_empty[11] ? "-" : your_score[11]);

        System.out.println("---------------------------");

        //야추 다이스의 꽃인 1~6번 보너스 점수(총합 63점 이상 시 +35점)도 넣어야함!!!!!!!
    }

    public void bot_turn() {
        System.out.println("\n 봇이 주사위를 굴립니다...");
        roll_dice();
        System.out.println("봇의 첫 번째 주사위: " + Arrays.toString(current_dice));

        // 봇의 간단한 리롤 전략 (랜덤하게 0~2개 선택하여 1번만 리롤)
        boolean[] bot_reroll = new boolean[5];
        for (int i = 0; i < 5; i++) {
            bot_reroll[i] = rand.nextBoolean() && rand.nextBoolean(); // 대략 25% 확률로 각 주사위 선택
        }
        reroll(bot_reroll);
        System.out.println("봇의 최종 주사위: " + Arrays.toString(current_dice));

        // 비어있는 칸 중 가장 점수가 높게 나오는 칸을 찾아서 등록
        int best_where = -1;
        int max_score = -1;
        for (int i = 1; i <= 12; i++) {
            if (is_bot_score_empty[i - 1]) {
                int expected = calculate_score(i);
                if (expected > max_score) {
                    max_score = expected;
                    best_where = i;
                }
            }
        }

        bot_score[best_where - 1] = max_score;
        is_bot_score_empty[best_where - 1] = false;
        System.out.printf(" 봇이 [%d]번 칸에 %d점으로 등록했습니다.\n", best_where, max_score);
    }

    public int get_total_score(int[] score, boolean[] is_empty) {
        int total = 0;
        for (int i = 0; i < 12; i++) {
            if (!is_empty[i]) total += score[i];
        }
        if (get_subtotal(score, is_empty) >= 63) {
            total += 35; // 보너스 점수 추가
        }
        return total;
    }

    public void main(String[] args) {

        yatch game = new yatch();

        for (int i = 0; i < 12; i++) {
            is_bot_score_empty[i] = true;
            is_your_score_empty[i] = true;
        }

        int select = 0;

        System.out.println("[ Yacht 주사위 게임 플레이 ]");

        for (int turn = 1; turn <= 12; turn++) {
            System.out.printf("\n ROUND %d / 12 \n", turn);
            game.scoreboard();

            System.out.println("\n 당신의 턴입니다. 첫 번째 주사위를 굴립니다.");
            game.roll_dice();

            // 총 3번의 기정 중 남은 2번의 리롤 기회 제공
            for (int chance = 1; chance <= 2; chance++) {
                System.out.println("현재 주사위: " + Arrays.toString(game.current_dice));
                boolean[] to_reroll = game.which_to_reroll();

                // 아무것도 선택하지 않으면 리롤 중단
                boolean anyTrue = false;
                for (int i = 0; i < 5; i++) if (to_reroll[i]) anyTrue = true;
                if (!anyTrue) break;

                game.reroll(to_reroll);
            }
            System.out.println("최종 주사위 결과: " + Arrays.toString(game.current_dice));

            int choice;
            while(true){
                System.out.print("점수를 등록할 카테고리 번호(1~12)를 입력하세요: ");
                choice = game.input.nextInt();

                if (choice >= 1 && choice <= 12 && game.is_your_score_empty[choice - 1]) {
                    break;
                }
                System.out.println("잘못된 번호이거나 이미 점수가 입력된 칸입니다. 다시 고르세요.");
            }
            game.enroll(choice);
            //플레이어 턴 끝, 봇 턴
            game.bot_turn();
            }

            System.out.println("\n 게임이 모두 종료되었습니다! 최종 결과 ");
            game.scoreboard();

            int myTotal = game.get_total_score(game.your_score, game.is_your_score_empty);
            int botTotal = game.get_total_score(game.bot_score, game.is_bot_score_empty);

            if (myTotal > botTotal) System.out.println(" 축하합니다! 당신이 승리했습니다!");
            else if (myTotal < botTotal) System.out.println(" 봇이 승리했습니다! 다음 기회에..");
            else System.out.println(" 비겼습니다! 엄청난 명승부였네요.");
         }

}

