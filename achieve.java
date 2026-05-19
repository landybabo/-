package game;

import java.io.*;

/*public class achieve {

    private static final String FILE_PATH = "achievements_save.txt"; //저장하고 불러올 경로
    private static final int ACHIEVEMENT_COUNT = 5; //업적갯수

    private boolean[] achievements;

    public achieve() {
        this.achievements = new boolean[ACHIEVEMENT_COUNT]; //생성자로 초기화
    }

    public void unlockAchievement(int index) {
        if (index >= 0 && index < achievements.length) {
            achievements[index] = true;
            System.out.println("[알림] " + index + "번 업적 달성!");
        }
    }

    public void printStatus() {
        System.out.print("현재 업적 상태: [");
        for (int i = 0; i < achievements.length; i++) {
            System.out.print(achievements[i] + (i < achievements.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }

    public static void save(boolean[] achieve, String filepath) throws IOException {
        try(BufferedWriter writer = new BufferedWriter((new FileWriter(filepath)))){
            StringBuilder dd = new StringBuilder();

            for(int i=0; i < achieve.length; i++){
                dd.append(achieve[i]);
                if(i < achieve.length - 1)  dd.append(",");
            }
            writer.write(dd.toString());
            System.out.println("저장 완료");

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void load()
}
*/
public class achieve {

    private static final String FILE_PATH = "achievements_save.txt";
    private static final int ACHIEVEMENT_COUNT = 5;

    // face 클래스 등에서 static으로 쉽게 접근할 수 있도록 인스턴스 배열을 static으로 변경하거나 관리
    private static boolean[] achievements = new boolean[ACHIEVEMENT_COUNT];

    public void unlockAchievement(int index) {
        if (index >= 0 && index < achievements.length) {
            achievements[index] = true;
            System.out.println("[알림] " + index + "번 업적 달성!");
        }
    }

    // static 메서드로 변경하여 어디서든 인스턴스 생성 없이 상태를 볼 수 있게 하기
    public static void printStatus() {
        System.out.print("현재 업적 상태: [");
        for (int i = 0; i < achievements.length; i++) {
            System.out.print(achievements[i] + (i < achievements.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }

    // Getter 추가 (저장할 때 데이터 가져오기용)
    public static boolean[] getAchievements() {
        return achievements;
    }

    public static void save(boolean[] achieve, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            StringBuilder dd = new StringBuilder();
            for (int i = 0; i < achieve.length; i++) {
                dd.append(achieve[i]);
                if (i < achieve.length - 1) dd.append(",");
            }
            writer.write(dd.toString());
            System.out.println("저장 완료");
        } catch (IOException e) {
            System.out.println("저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 미완성된 load() 메서드 구현
    public static void load(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] tokens = line.split(",");
                for (int i = 0; i < Math.min(tokens.length, ACHIEVEMENT_COUNT); i++) {
                    achievements[i] = Boolean.parseBoolean(tokens[i]);
                }
                System.out.println("불러오기 완료!");
            }
        } catch (IOException e) {
            System.out.println("저장된 파일이 없거나 읽기 오류가 발생했습니다.");
        }
    }
}