package game;

import java.util.Scanner;
import java.security.SecureRandom;



//완성도가 매우 떨어집니다.. 너무 대충 만든감이 없잖아 있는... 죄송합니다.
public class blackjack extends games {
    Scanner input = new Scanner(System.in);
    SecureRandom rand = new SecureRandom();

    int your_balance = 10000;
    int current_bet = 0;
    int total_rounds = 0;

    int your_card_count = 0;
    int dealer_card_count = 0;

    int[] your_deck = new int[11];
    int[] your_deck_suit = new int[11]; //스페이드, 클로버 , 하트 , 다이아몬드 각각 0 1 2 3

    int[] dealer_deck = new int[11];
    int[] dealer_deck_suit = new int[11];

    enum stance {STAY, STAND, DOUBLE_DOWN, HIT, BURST}

    String[] suit_names = {"스페이드", "클로버", "하트", "다이아몬드"};
    String[] card_ranks = {"", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public void drawCard(int[] deck, int[] deck_suit, boolean isPlayer) {
        int count = isPlayer ? your_card_count : dealer_card_count;

        int tmp = rand.nextInt(208); // 0 ~ 207 , 13 * 4 * 4 = 208

        // 0:스페이드, 1:클로버, 2:하트, 3:다이아몬드 (0~3 범위로 제한)
        int suit = (tmp / 52) % 4;
        // 1 ~ 13 (A, 2~10, J, Q, K)
        int rank = (tmp % 13) + 1;

        deck[count] = rank;
        deck_suit[count] = suit;

        if (isPlayer) {
            your_card_count++;
            System.out.printf(" 플레이어 : %s %s을(를) 뽑았습니다.\n", suit_names[suit], card_ranks[rank]);
        } else {
            dealer_card_count++;
            // 딜러카드 공개.
            System.out.printf(" 딜러 : %s %s을(를) 뽑았습니다.\n", suit_names[suit], card_ranks[rank]);
        }
    }

    public void first_give() {
        int tmp = 0;
        System.out.println("카드 두장씩.");

        drawCard(your_deck, your_deck_suit, true);
        drawCard(your_deck, your_deck_suit, true);

        drawCard(dealer_deck, dealer_deck_suit, false);
        drawCard(dealer_deck, dealer_deck_suit, false);
    }

    public void hit() {
        //임의의 카드 한장 지급하는 메소드.
    }

    public void roundset() {
        //몇라운드 하고싶은지 정하게하기
        System.out.print("몇 라운드를 플레이하시겠습니까 : ");
        total_rounds = input.nextInt();
    }

    public void bet() {
        //기본금 10000원 주는데, 게임 시작전 얼마 걸지 정하게하기.
        while (true) {
            System.out.printf("\n현재 잔고: %d원\n", your_balance);
            System.out.print("이번 라운드에 얼마를 베팅 하시겠습니까 : ");

            current_bet = input.nextInt();

            if (current_bet > 0 && current_bet <= your_balance) {
                your_balance -= current_bet;
                System.out.printf("%d원 베팅 완료 (남은 잔고: %d원)\n", current_bet, your_balance);
                break;
            }
            System.out.println("잘못된 베팅 금액입니다. 잔고 내에서 다시 입력해주세요.");
        }
    }

    public int calculate_score(int[] deck, int card_count) {
        int score = 0;
        int aceCount = 0;

        for (int i = 0; i < card_count; i++) {
            int rank = deck[i];
            if (rank == 1) {      // Ace
                aceCount++;
                score += 11;      // 일단 11점으로 계산 > 굳
            } else if (rank >= 10) { // 10, J, Q, K
                score += 10;
            } else {
                score += rank;
            }
        }

        // 총점이 21점을 초과하고 Ace가 있다면, Ace를 1점으로 낮춤
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    public void player_choice() {
        //더블다운, 스탠드, 히트, 다이 중에 고르게하기.
        while (true) {
            int playerScore = calculate_score(your_deck, your_card_count);
            System.out.printf("\n당신의 현재 점수: %d\n", playerScore);

            if (playerScore > 21) {
                System.out.println("21점을 초과했습니다! 버스트(BURST)!");
                return;
            } else if (playerScore == 21) {
                System.out.println("블랙잭 / 21점 달성!");
                return;
            }

            System.out.print("선택하세요 (1. 히트(Hit)  2. 스탠드(Stand)  3. 더블다운(Double Down)): ");
            int choice = input.nextInt();

            if (choice == 1) {
                drawCard(your_deck, your_deck_suit, true);
            } else if (choice == 2) {
                System.out.println("차례를 마칩니다(Stand).");
                return;
            } else if (choice == 3) {
                if (your_balance >= current_bet) {
                    your_balance -= current_bet;
                    current_bet *= 2;
                    System.out.printf("더블다운! 베팅액이 두 배가 됩니다 (총 베팅: %d원)\n", current_bet);
                    drawCard(your_deck, your_deck_suit, true);
                    return; // 더블다운은 한 장만 더 받고 강제 스탠드
                } else {
                    System.out.println("잔고가 부족하여 더블다운을 할 수 없습니다.");
                }
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    public void resetRound() {
        your_card_count = 0;
        dealer_card_count = 0;
        current_bet = 0;
        your_deck = new int[11];
        dealer_deck = new int[11];
    }


    public void dealer_choice() {
        //16 이하면 뽑고, 17 이상이면 스탠드.
        System.out.println("\n--- 딜러의 차례 ---");
        while (true) {
            int dealerScore = calculate_score(dealer_deck, dealer_card_count);
            System.out.printf("딜러의 현재 점수: %d\n", dealerScore);

            if (dealerScore > 21) {
                System.out.println("딜러 버스트(BURST)!");
                return;
            }

            if (dealerScore <= 16) {
                System.out.println("딜러가 16점 이하이므로 카드를 한 장 더 뽑습니다.");
                drawCard(dealer_deck, dealer_deck_suit, false);
            } else {
                System.out.println("딜러가 17점 이상이므로 스탠드(Stand)합니다.");
                return;
            }
        }
    }


    public void startGame() {
        
        achieve.unlockAchievement(0); 

        System.out.println("[ BlackJack 게임 플레이 ]");
        roundset();

        for (int round = 1; round <= total_rounds; round++) {
            if (your_balance <= 0) {
                System.out.println("\n 가진 돈을 모두 잃었습니다. 패배.. ( 강제종료 )");
                break;
            }

            System.out.printf("\n====== ROUND %d / %d ======\n", round, total_rounds);
            resetRound();
            bet();
            first_give();

            //플레이어턴
            player_choice();
            int playerScore = calculate_score(your_deck, your_card_count);

            //버스트 안하면 딜러 카드뽑음
            if (playerScore <= 21) {
                dealer_choice();
                int dealerScore = calculate_score(dealer_deck, dealer_card_count);

                System.out.println("\n--- 최종 결과 발표 ---");
                System.out.printf("당신의 점수: %d | 딜러의 점수: %d\n", playerScore, dealerScore);

                if (dealerScore > 21 || playerScore > dealerScore) {
                    System.out.printf("축하합니다! 승리하셨습니다. (%d원 획득)\n", current_bet * 2);
                    your_balance += (current_bet * 2);
                } else if (playerScore < dealerScore) {
                    System.out.println("딜러가 승리했습니다. 베팅 금액을 잃었습니다.");
                } else {
                    System.out.println("비겼습니다. 베팅 금액을 돌려받습니다.");
                    your_balance += current_bet;
                }
            } else {
                System.out.println("\n당신의 버스트로 인해 딜러가 자동으로 승리했습니다.");
            }
        }
        System.out.printf("  게임종료. 최종 잔고: %d원\n", your_balance);
        }
    }

