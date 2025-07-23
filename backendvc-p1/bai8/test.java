package bai8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) {
        List<City> cities = new ArrayList<>();
        List<Country> countries = new ArrayList<>();

        try {
            // Đọc file cities.dat
            Scanner cityScanner = new Scanner(new File("E:\\backendvc\\bai8\\cities.dat"));
            while (cityScanner.hasNextLine()) {
                String line = cityScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                filterCity(cities,line);
            }
            cityScanner.close();

            // Đọc file countries.dat
            Scanner countryScanner = new Scanner(new File("E:\\backendvc\\bai8\\countries.dat"));
            while (countryScanner.hasNextLine()) {
                String line = countryScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                filterCountry(countries,line);
            }
            countryScanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("❌ Không tìm thấy file: " + e.getMessage());
        }
        List<City> thanhPhoDongNhatQuocGiaList=thanhPhoDongNhatQuocGia(cities,countries);
        thanhPhoDongNhatLucDia(thanhPhoDongNhatQuocGiaList,countries);
        thuDoDongNhat(cities,countries);
        thuDoDongNhatLucDia(cities,countries);
        quocGiaThanhPhoGiamDan(cities,countries);
        sapXepQuocGiaTheoMatDoDanSo(countries);
    }

    public static List<City> thanhPhoDongNhatQuocGia(List<City> cityList,List<Country> countryList){
        List<City> thanhPhoDongNhatQuocGiaList=new ArrayList<>();
        for(Country c:countryList){
            List<City> l=cityList.stream().filter(x-> x.getCountryCode().equals(c.getCode()))
                    .sorted((x,y)-> x.getPopulation()-y.getPopulation())
                    .collect(Collectors.toList());
            System.out.println("Quoc gia: " + c.getName());
            if(l.size()>0) {
                System.out.println(l.get(l.size()-1));
                thanhPhoDongNhatQuocGiaList.add(l.get(l.size()-1));
            }
        }
        return thanhPhoDongNhatQuocGiaList;
    }
    public static void thanhPhoDongNhatLucDia(List<City> thanhPhoDongNhatQuocGia,List<Country> countryList){
        List<String> khuvuc= new ArrayList<>();
        khuvuc.addAll(List.of(new String[]{"Asia", "Europe", "Africa", "North America", "South America", "Oceania"}));
        for(String s:khuvuc){
            List<String> asia = countryList.stream().filter(x->x.getContinent().equals(s))
                    .map(Country::getCode)
                    .collect(Collectors.toList());
            List<City> cityAsia=thanhPhoDongNhatQuocGia.stream().filter(c -> asia.contains(c.getCountryCode()))
                    .sorted((x,y) -> x.getPopulation()-y.getPopulation())
                    .collect(Collectors.toList());
            System.out.println(s+": " + cityAsia.getLast());
        }

    }
    public static void thuDoDongNhat(List<City> cityList,List<Country> countryList){
        List<Integer> codeThuDo =countryList.stream().map(Country::getCapital).collect(Collectors.toList());
        List<City> thuDoDongNhat=cityList.stream().filter(x->codeThuDo.contains(x.getId()))
                .sorted((x,y)->x.getPopulation()-y.getPopulation())
                .collect(Collectors.toList());
        System.out.println(thuDoDongNhat.getLast());
    }
    public static void thuDoDongNhatLucDia(List<City> cityList,List<Country> countryList){
        Set<String> lucDia =countryList.stream().map(Country::getContinent).collect(Collectors.toSet());
        for(String s:lucDia){
            List<Integer> countries=countryList.stream().filter(x->x.getContinent().equals(s)).map(Country::getCapital).collect(Collectors.toList());
            Optional<City> maxCity= cityList.stream().filter(x ->countries.contains(x.getId())).max(Comparator.comparingInt(City::getPopulation));
            maxCity.ifPresent(city ->
                    System.out.printf("Lục địa %-12s → %-20s %,d người%n",
                            s, city.getName(), city.getPopulation()));
        }
    }
    public static void quocGiaThanhPhoGiamDan(List<City> cityList,List<Country> countryList){
        Map<String, Long> soLuongThanhPho = cityList.stream()
                .collect(Collectors.groupingBy(City::getCountryCode, Collectors.counting()));
        List<Country> ketQua=countryList.stream().sorted((o1,o2) -> {
            long count1=soLuongThanhPho.getOrDefault(o1.getCode(),0L);
            long count2=soLuongThanhPho.getOrDefault(o2.getCode(),0L);
            return Long.compare(count2, count1); // giảm dần
        }).collect(Collectors.toList());
        for(Country x:ketQua){
            System.out.println("Nước "+x.getName()+ " có "+ soLuongThanhPho.getOrDefault(x.getCode(),0L)+ " thành phố" );
        }
    }
    public static void sapXepQuocGiaTheoMatDoDanSo(List<Country> countryList) {
        List<Country> ketQua = countryList.stream()
                .filter(c -> c.getPopulation() > 0 && c.getSurfaceArea() > 0) // loại bỏ dân số hoặc diện tích bằng 0
                .sorted((c1, c2) -> {
                    double density1 = c1.getPopulation() / c1.getSurfaceArea();
                    double density2 = c2.getPopulation() / c2.getSurfaceArea();
                    return Double.compare(density2, density1); // sắp xếp giảm dần
                })
                .collect(Collectors.toList());

        for (Country c : ketQua) {
            double density = c.getPopulation() / c.getSurfaceArea();
            System.out.printf("%-30s (Mật độ: %.2f người/km²)%n", c.getName(), density);
        }
    }

    private static void filterCountry(List<Country> countries, String line) {
        line = line.replace("Country{", "").replace("}", "");
        String[] parts = line.split(", ");
        String code = parts[0].split("=")[1];
        String name = parts[1].split("=")[1];
        String continent = parts[2].split("=")[1];
        double surfaceArea = Double.parseDouble(parts[3].split("=")[1]);
        int population = Integer.parseInt(parts[4].split("=")[1]);
        double gnp = Double.parseDouble(parts[5].split("=")[1]);
        int capital = Integer.parseInt(parts[6].split("=")[1]);

        countries.add(new Country(code, name, continent, surfaceArea, gnp, population,capital));
    }

    private static void filterCity(List<City> cities, String line) {
        // Loại bỏ phần "City [" và "]"
        line = line.replace("City [", "").replace("]", "");
        String[] parts = line.split(", ");
        int id = Integer.parseInt(parts[0].split("=")[1]);
        String name = parts[1].split("=")[1];
        int population = Integer.parseInt(parts[2].split("=")[1]);
        String countryCode = parts[3].split("=")[1];

        cities.add(new City(id,  population, name,countryCode));
    }
}