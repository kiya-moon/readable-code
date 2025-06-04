package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * 이름 변경 : shift + F6(macOS), F6 (Windows/Linux)
 * 메서드 추출 : option + command + M (macOS), ctrl + alt + M (Windows/Linux)
 * 상수로 추출 : option + command + C (macOS), ctrl + alt + C (Windows/Linux)
 * try ~ catch 블록 추가 : option + command + T (macOS), ctrl + alt + T (Windows/Linux)
 */
public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LAND_MINE_COUNT = 10;
    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                System.out.println("   a b c d e f g h i j");
                showBoard();

                if (doesUserWinTheGame()) { // 구체적이었던 조건문을 추상화하여 추상레벨을 맞춰주기
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                System.out.println();

//            Scanner scanner = new Scanner(System.in);   // Scanner 선언과 사용하는 곳이 멀어 가까이로 가져왔지만, for문 안이기 때문에 매번 선언이 됨
                // >>> Scanner는 한 번만 선언하면 재사용 할 수 있기 때문에 상수화
                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {    // 사용자가 지뢰 cell을 밟았다면 game over
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        int selectedColIndex = convertColFrom(cellInputCol);    // 메서드명과 파라미터를 연결 지어서 변경. cellInputCol로부터 컬럼 변환할거야
        return selectedColIndex;
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

//    private static void checkIfAllCell() {
//        boolean isAllOpened = isAllOpened();    // cell이 모두 오픈되어 있는 지 체크하는 역할
//        if (isAllOpened) {
//            gameStatus = 1; // 게임 종료 선언 역할
//        }
//    }   // >>> 한 메서드 안에 두 개의 역할이 있음

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();    // check~ 라는 이름의 메서드는 보통 void 타입이기 때문에, boolean 타입을 반환하는 역할에 맞게 이름 변경
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }   // >>> 역할에 맞도록 이름 변경

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    // 메서드 리팩토링 Tip!
    // 리팩토링 할 메서드가 여러 곳에서 사용하고 있다면 바꾸었을 때 사이드 이펙트가 발생할 수 도 있다.
    // 따라서, 리팩토링 할 메서드를 복제해서 수정 후, 수정한 메서드로 변경 + 테스트를 반복해서 최종적으로 적용해야 한다.
    private static boolean isAllCellOpened() {  // 중첩 반복문을 사용하지 않고, 스트림을 사용하여 가독성을 높임
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(CLOSED_CELL_SIGN::equals); // 모든 cell이 오픈되어 있는지 확인
    }

    private static int convertRowFrom(char cellInputRow) {  // 코드의 양으로 추상화 여부를 결정하는 게 아니라, 같은 메서드 내에서 추상화 레벨을 맞춰준다는 목적으로 접근해야 한다
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
//                selectedColIndex = 0;
//                break;
                return 0;
            case 'b':
                return 1;
            case 'c':
                 return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) { // 반목문의 i, j를 의미 있는 단어로 변경
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = CLOSED_CELL_SIGN;
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {  // 여기 i는 그냥 단순 반복 횟수를 의미 하므로 수정x
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    NEARBY_LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                NEARBY_LAND_MINE_COUNTS[row][col] = count;
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;

            if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
                count++;
            }
            if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
                count++;
            }
            if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
                count++;
            }
            if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
                count++;
            }
            if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
                count++;
            }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    // 재귀함수 - 종단 조건 설정을 잘 해놓지 않는다면 스택 오버 플로우 등이 발생할 수 있다
    // 재귀적으로 돌면서 주변에 인접한 열 수 있는 모든 cell을 오픈하는 함수
    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {  // 보드 범위를 벗어나면 정상적이지 않으므로 return
            return;
        }
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) { // 이미 오픈된 cell이라면 return
            return;
        }
        if (isLandMineCell(row, col)) {  // 지뢰 cell이라면 return
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {    // 지뢰 카운트를 갖고 있는 cell이라면 지뢰가 몇 개 있는지 숫자 표시하고 return
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPENED_CELL_SIGN;  // 아무것도 아니라면 빈 cell로 표시
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
