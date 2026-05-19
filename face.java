/*package game;

import java.util.Scanner;

import static java.lang.System.exit;

public class face {

    public static void gamelist(){

        System.out.print("\n-------------------------------------------------------------\n");

        System.out.println("1. yacht");
        System.out.println("2. 블랙잭");
        System.out.println("추후에 추가예정..");

        System.out.println("-------------------------------------------------------------");
    }

    public static void list(){

        System.out.println("\n-------------------------------------------------------------");

        System.out.println("1. 게임 목록보기");
        System.out.println("2. 스코어보드(업적) 보기");
        System.out.println("3. 불러오기");
        System.out.println("4. 저장하기");
        System.out.println("5. 게임 플레이");
        System.out.println("6. 종료 ");
        System.out.println("\n-------------------------------------------------------------");
    }

    public static void execut(){

    }


    public int ask(){
        Scanner input = new Scanner(System.in);
        int select = 0;
        int savecnt = 0;

        System.out.println("환영합니다. 무엇을 하고 싶으신가요?");
        list();

        while(select == 6){

            System.out.print("입력 : ");
            select = input.nextInt();

            achieve d = new achieve();

            switch(select){
                case 1:
                    gamelist();
                    break;
                case 2:
                    achieve.printStatus();
                    break;
                case 3:
                    //로드
                    break;
                case 4:
                    //세이브
                    break;
                case 5:
                    //게임 플레이인데.. 흠.
                    System.out.println("\n어떤 게임을 플레이 하고 싶으신가요?");
                    System.out.println("입력 : ");
                    select = input.nextInt();

                    if(select == 1 | select == 2)   return select;
                    else System.out.println("잘못된 입력");
                case 6:
                    exit(1);
            }
            System.out.println("\n무엇을 하고 싶으신가요");

        }
    }
}
*/
package game;

import java.util.Scanner;

public class face {

    public static void gamelist() {
        System.out.print("\n-------------------------------------------------------------\n");
        System.out.println("1. yacht");
        System.out.println("2. 블랙잭");
        System.out.println("추후에 추가예정..");
        System.out.println("-------------------------------------------------------------");
    }

    public static void list() {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("1. 게임 목록보기");
        System.out.println("2. 스코어보드(업적) 보기");
        System.out.println("3. 불러오기");
        System.out.println("4. 저장하기");
        System.out.println("5. 게임 플레이");
        System.out.println("6. 종료 ");
        System.out.println("\n-------------------------------------------------------------");
    }

    public int ask() {
        Scanner input = new Scanner(System.in);
        int select = 0;

        while (true) {
            System.out.println("환영합니다. 무엇을 하고 싶으신가요?");
            list();
            System.out.print("입력 : ");
            select = input.nextInt();

            switch (select) {
                case 1:
                    gamelist();
                    break;
                case 2:
                    achieve.printStatus(); // static 메서드 호출
                    break;
                case 3:
                    achieve.load("achievements_save.txt");
                    break;
                case 4:
                    achieve.save(achieve.getAchievements(), "achievements_save.txt");
                    break;
                case 5:
                    System.out.println("\n어떤 게임을 플레이 하고 싶으신가요?");
                    gamelist();
                    System.out.print("입력 : ");
                    int gameSelect = input.nextInt();

                    if (gameSelect == 1 || gameSelect == 2) {
                        return gameSelect; // 선택한 게임 번호를 excutable로
                    } else {
                        System.out.println("잘못된 입력입니다.");
                    }
                    break;
                case 6:
                    System.out.println("게임을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("메뉴판에 있는 번호를 입력해주세요.");
            }
            System.out.println("\n=============================================");
        }
    }
}