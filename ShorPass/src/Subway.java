/**
 *
 */

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * desc：利用Dijkstra算法，计算地铁站经过路径，以南京地铁为例
 *
 * @author chaisson
 * @since 2015-5-31 上午9:43:38
 */
public class Subway {

    private List<Station> outList = new ArrayList<Station>();//记录已经分析过的站点

    /**
     * @param s1 起点站
     * @param s2 终点站
     */
    //计算从s1站到s2站的最短经过路径
    public void calculate(Station s1, Station s2) {
        if (outList.size() == DataBuilder.totalStation) {
            System.out.println("111找到目标站点：" + s2.getName() + "，共经过" + (s1.getAllPassedStations(s2).size() - 1) + "站");
            for (Station station : s1.getAllPassedStations(s2)) {
                System.out.print(station.getName() + "->");
            }
            System.out.println();
            System.out.print("总距离=" + getDistance(s1.getAllPassedStations(s2)));
            return;
        }
        if (!outList.contains(s1)) {
            outList.add(s1);
        }
        //如果起点站的OrderSetMap为空，则第一次用起点站的前后站点初始化之
        if (s1.getOrderSetMap().isEmpty()) {
            //与起点站相连的所有站点集合
            List<Station> Linkedstations = getAllLinkedStations(s1);
            for (Station s : Linkedstations) {
                s1.getAllPassedStations(s).add(s);
            }
        }
        Station parent = getShortestPath(s1);//获取距离起点站s1最近的一个站（有多个的话，随意取一个）
//        if (parent != null && parent.getName().equals(s2.getName())) {
//            System.out.println("222找到目标站点：" + s2 + "，共经过" + (s1.getAllPassedStations(s2).size() - 1) + "站");
//            for (Station station : s1.getAllPassedStations(s2)) {
//                System.out.print(station.getName() + "->");
//            }
//            return;
//        }
        for (Station child : getAllLinkedStations(parent)) {
            if (outList.contains(child)) {
                continue;
            }
//            int shortestPath = (s1.getAllPassedStations(parent).size() - 1) + 1;//前面这个1表示计算路径需要去除自身站点，后面这个1表示增加了1站距离
            int shortestPath = getDistance(s1.getAllPassedStations(parent));//前面这个1表示计算路径需要去除自身站点，后面这个1表示增加了1站距离
            if (s1.getAllPassedStations(child).contains(child)) {
                //如果s1已经计算过到此child的经过距离，那么比较出最小的距离
//                int toChildStationSize = s1.getAllPassedStations(child).size();
                int toChildStationSize = getDistance(s1.getAllPassedStations(child));
//                if ((toChildStationSize - 1) > shortestPath) {
                if (toChildStationSize > shortestPath) {
                    //重置S1到周围各站的最小路径
                    s1.getAllPassedStations(child).clear();
                    s1.getAllPassedStations(child).addAll(s1.getAllPassedStations(parent));
                    s1.getAllPassedStations(child).add(child);
                }
            } else {
                //如果s1还没有计算过到此child的经过距离
                s1.getAllPassedStations(child).addAll(s1.getAllPassedStations(parent));
                s1.getAllPassedStations(child).add(child);
            }
        }
        outList.add(parent);
        calculate(s1, s2);//重复计算，往外面站点扩展
    }

    //取参数station到各个站的最短距离，相隔1站，距离为1，依次类推
    private Station getShortestPath(Station station) {
        int minPatn = Integer.MAX_VALUE;
        Station rets = null;
        for (Station s : station.getOrderSetMap().keySet()) {
//            System.out.println("相邻站点 ："+s.getName());
            if (outList.contains(s)) {
                continue;
            }
            LinkedHashSet<Station> set = station.getAllPassedStations(s);//参数station到s所经过的所有站点的集合
            int distance = getDistance(set);
            if (distance < minPatn) {
                minPatn = distance;
                rets = s;
            }
//            if (set.size() < minPatn) {
//                minPatn = set.size();
//                rets = s;
//            }
        }
        return rets;
    }

    /**
     * 经过A,B线的换乘站X去往B线的Y站时，找到B线的X站，用于计算距离
     *
     * @param s1 换乘站 X
     * @param s2 目标站 Y
     * @return
     */
    private Station getNeighborStation(Station s1, Station s2) {
        for (List<Station> line : DataBuilder.lineSet) {
            if (line.contains(s2)) {//如果某一条线包含了此站，注意由于重写了hashcode方法，只有name相同，即认为是同一个对象
                Station s = line.get(line.indexOf(s2));
                if (s.prev != null && s.prev.getName().equalsIgnoreCase(s1.getName())) {
                    return line.get(line.indexOf(s2) - 1);
                } else if (s.next != null && s.next.getName().equalsIgnoreCase(s1.getName())) {
                    return line.get(line.indexOf(s2) + 1);
                }
            }
        }
        return null;
    }

    /**
     * 获取容器内经过站点的距离
     *
     * @param set
     * @return
     */
    private int getDistance(LinkedHashSet<Station> set) {
        int distance = 0;
        try {
            List<Station> list = new ArrayList<>();
            list.addAll(set);
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    Station currentStation = list.get(i);
                    Station nextStation = list.get(i + 1);
                    if (currentStation.next.getName().equals(nextStation.getName())) {
                        distance += currentStation.nextDistance;
                    } else {
                        Station targetLineNeighStation = getNeighborStation(currentStation, nextStation);
                        if (targetLineNeighStation != null) {
                            if (targetLineNeighStation.prev != null && targetLineNeighStation.prev.getName().equalsIgnoreCase(nextStation.getName())) {
                                distance += targetLineNeighStation.preDistance;
                            } else {
                                distance += targetLineNeighStation.nextDistance;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return distance;
    }

    //获取参数station直接相连的所有站，包括交叉线上面的站
    private List<Station> getAllLinkedStations(Station station) {
        List<Station> linkedStations = new ArrayList<Station>();
        for (List<Station> line : DataBuilder.lineSet) {
            if (line.contains(station)) {//如果某一条线包含了此站，注意由于重写了hashcode方法，只有name相同，即认为是同一个对象
                Station s = line.get(line.indexOf(station));
                if (s.prev != null) {
                    linkedStations.add(s.prev);
                }
                if (s.next != null) {
                    linkedStations.add(s.next);
                }
            }
        }
        return linkedStations;
    }

    /**
     * desc: How to use the method
     * author chaisson
     * since 2015-5-31
     * version 1.0
     */

    public static void main(String[] args) {
        System.out.println(DataBuilder.totalStation);

        List<Station> calLine = DataBuilder.line2;
        for (int i = 0; i < calLine.size(); i++) {
            System.out.println(calLine.get(i).getName());
        }
        for (int i = 0; i < calLine.size(); i++) {
            System.out.println(calLine.get(i).preDistance);
        }
        System.out.println("-------------");
        long t1 = System.currentTimeMillis();
        Subway sw = new Subway();
        System.out.println("READY---");
        sw.calculate(DataBuilder.line1.get(1), DataBuilder.line1.get(7));
        long t2 = System.currentTimeMillis();
        System.out.println();
        System.out.println("耗时：" + (t2 - t1) + "ms");
    }
}

