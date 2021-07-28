package net.codejava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.codejava.model.*;
import net.codejava.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @Autowired
    private StorageProductService service;

    @Autowired
    private TempBalanceService tempBalanceService;

    @Autowired
    private TempReplaceService tempReplaceService;

    @Autowired
    private ServiceProductService serviceProductService;


    @Autowired
    private BalanceProductService balanceProductService;
    @Autowired
    private ReplaceProductService replaceProductService;

    @Autowired
    private StorageHistoryService storageHistoryService;


    @RequestMapping("/storage")
    public String viewStoragePage(Model model) {
        List<Product> listProducts = service.listAll();
        listProducts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        Collections.reverse(listProducts);
        model.addAttribute("listProducts", listProducts);

        return "product";
    }

    @RequestMapping("/balance")
    public String viewBalancePage(Model model) {
        List<BalanceProduct> balanceProducts = new ArrayList<>();
        if (balanceProductService.getSize() != 0) {
            Date lastDate = balanceProductService.lastBalanceDate();
            if (lastDate != null) {
                balanceProducts = balanceProductService.listAllByDate(lastDate);
                Collections.reverse(balanceProducts);

            }

        }

        model.addAttribute("balanceProducts", balanceProducts);

        return "balance";
    }

    @RequestMapping("/consume")
    public String viewConsumePage(Model model) {

        return "consume";
    }

    @RequestMapping("/service")
    public String viewServicePage(Model model) {
        return "service";
    }

    @RequestMapping("/new")
    public String showNewProductPage(Model model) {
        ProductModel product = new ProductModel();
        model.addAttribute("product", product);

        return "new_product";
    }

    @RequestMapping("/newService")
    public String showNewServicePage(Model model) {
        ServiceModel service = new ServiceModel();
        model.addAttribute("service", service);

        return "new_service";
    }

    @RequestMapping("/serviceHistory")
    public String showServiceHistoryPage(Model model) throws ParseException {
        DateModel dateModel = new DateModel();
        if (dateModel.getStartDate() == null || dateModel.getStartDate().isEmpty()) {
            dateModel.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        if (dateModel.getEndDate() == null || dateModel.getEndDate().isEmpty()) {
            dateModel.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getStartDate()));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getEndDate()));

        List<Service> serviceList = serviceProductService.listAllByDate(calendar.getTime(), calendarend.getTime());
        serviceList.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        Collections.reverse(serviceList);

        model.addAttribute("serviceHistoryList", serviceList);
        long totalPrice = 0;
        for (Service service : serviceList) {
            totalPrice += service.getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);

        ServiceFilter filter = new ServiceFilter();
        model.addAttribute("filter", filter);

        return "service_history";
    }

    @RequestMapping("/serviceHistoryByDate")
    public String showServiceHistoryByDatePage(Model model, @ModelAttribute("filter") ServiceFilter filter) throws ParseException {
        String date1 = filter.getDate1();
        String date2 = filter.getDate2();
        if (date1 == null || date1.isEmpty()) {
            date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (date2 == null || date2.isEmpty()) {
            date2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        int in = 0;
        int out = 0;

        String name = "";

        if (filter.getFilterServiceName() != null) {
            name = filter.getFilterServiceName();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date1));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date2));
        if (calendar.getTime().getTime() > calendarend.getTime().getTime()) {
            Calendar temp = Calendar.getInstance();
            temp = calendar;
            calendar = calendarend;
            calendarend = temp;
        }


        List<Service> serviceHistoryList = serviceProductService.listAllByDateName(calendar.getTime(), calendarend.getTime(), name);
        serviceHistoryList.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        Collections.reverse(serviceHistoryList);

        model.addAttribute("serviceHistoryList", serviceHistoryList);
        long totalPrice = 0;
        for (Service service : serviceHistoryList) {
            totalPrice += service.getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("filter", filter);


        return "service_history";
    }


    @RequestMapping("/storageHistory")
    public String showStorageHistoryPage(Model model) throws ParseException {
        DateModel dateModel = new DateModel();
        if (dateModel.getStartDate() == null || dateModel.getStartDate().isEmpty()) {
            dateModel.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        if (dateModel.getEndDate() == null || dateModel.getEndDate().isEmpty()) {
            dateModel.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        int in = 0;
        int out = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getStartDate()));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getEndDate()));

        List<StorageHistory> storageHistoryList = storageHistoryService.listAllByDate(calendar.getTime(), calendarend.getTime());
        storageHistoryList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        Collections.reverse(storageHistoryList);

        model.addAttribute("storageHistoryList", storageHistoryList);
        for (StorageHistory storage : storageHistoryList) {
            if (storage.isIn()) {
                in += storage.getTotalPrice();
            } else {
                out += storage.getTotalPrice();
            }

        }

        String date1 = dateModel.getStartDate();
        String date2 = dateModel.getEndDate();
        model.addAttribute("in", in);
        model.addAttribute("out", out);

        Filter filter = new Filter();
        model.addAttribute("filter", filter);
        return "storage_history";
    }

    @RequestMapping("/storageHistoryByDate")
    public String showStorageHistoryByDatePage(Model model, @ModelAttribute("filter") Filter filter, @RequestParam(value = "action", required = true) String action) throws ParseException {
        String date1 = filter.getDate1();
        String date2 = filter.getDate2();
        if (date1 == null || date1.isEmpty()) {
            date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (date2 == null || date2.isEmpty()) {
            date2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        int in = 0;
        int out = 0;

        String name = "";
        String company = "";
        String productType = "";
        if (filter.getFilterProductName() != null) {
            name = filter.getFilterProductName();
        }
        if (filter.getFilterCompanyName() != null) {
            company = filter.getFilterCompanyName();
        }
        if (filter.getProductType() != null) {
            productType = filter.getProductType();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date1));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date2));
        if (calendar.getTime().getTime() > calendarend.getTime().getTime()) {
            Calendar temp = Calendar.getInstance();
            temp = calendar;
            calendar = calendarend;
            calendarend = temp;
        }


        List<StorageHistory> storageHistoryList = storageHistoryService.listAllByDateNameCompanyIn(calendar.getTime(), calendarend.getTime(), name, company, filter.isFilterProductIn(), productType);
        storageHistoryList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        Collections.reverse(storageHistoryList);

        for (StorageHistory storage : storageHistoryList) {
            if (storage.isIn()) {
                in += storage.getTotalPrice();
            } else {
                out += storage.getTotalPrice();
            }

        }
        model.addAttribute("in", in);
        model.addAttribute("out", out);

        model.addAttribute("filter", filter);

        List<StorageHistory> newList = new ArrayList<>();
        if (action.equals("FilterMerge")) {
            for (StorageHistory storage : storageHistoryList) {
                StorageHistory filtered = newList.stream().filter(carnet -> storage.getProductId().equals(carnet.getProductId())).findFirst().orElse(null);
                if (filtered == null) {
                    StorageHistory storageHistory = new StorageHistory();
                    storageHistory.setPrice(storage.getPrice());
                    storageHistory.setProductType(storage.getProductType());
                    storageHistory.setName(storageHistory.getName());
                    storageHistory.setProductId(storage.getProductId());
                    storageHistory.setCount(storage.getCount());
                    storageHistory.setIn(storage.isIn());
                    storageHistory.setCompany(storage.getCompany());
                    storageHistory.setTotalPrice(storage.getTotalPrice());
                    storageHistory.setUnit(storage.getUnit());
                    storageHistory.setDate(storage.getDate());
                    newList.add(storage);
                } else {
                    //we have this product

                    int location = newList.indexOf(filtered);

                    if (filtered.getPrice() != storage.getPrice()) {
                        //avg price
                        double totalCount = filtered.getCount() + storage.getCount();
                        double totalPrice = Math.round((filtered.getPrice() * filtered.getCount()) + (storage.getPrice() * storage.getCount()));
                        double avg = Math.round(totalPrice / totalCount);
                        filtered.setCount(totalCount);
                        filtered.setPrice(avg);
                        filtered.setTotalPrice(totalCount * avg);

                    } else {
                        //same price
                        double totalCount = filtered.getCount() + storage.getCount();
                        double totalPrice = filtered.getPrice();
                        filtered.setCount(totalCount);
                        filtered.setPrice(totalPrice);
                        filtered.setTotalPrice(totalCount * totalPrice);


                    }
                    newList.set(location, filtered);


                }

            }
            storageHistoryList = newList;
        }
        model.addAttribute("storageHistoryList", storageHistoryList);

        return "storage_history";
    }

    @RequestMapping("/consumeHistory")
    public String showConsumeHistoryPage(Model model) throws ParseException {
        DateModel dateModel = new DateModel();
        if (dateModel.getStartDate() == null || dateModel.getStartDate().isEmpty()) {
            dateModel.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        if (dateModel.getEndDate() == null || dateModel.getEndDate().isEmpty()) {
            dateModel.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        int out = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getStartDate()));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateModel.getEndDate()));

        List<BalanceProduct> balanceProducts = balanceProductService.getAllIfConsumedByDate(calendar.getTime(), calendarend.getTime());

        balanceProducts.sort((o1, o2) -> o1.getBalanceDate().compareTo(o2.getBalanceDate()));
        Collections.reverse(balanceProducts);

        int count = 0;
        Set<Integer> counts = new HashSet<>();
        model.addAttribute("balanceProducts", balanceProducts);
        for (BalanceProduct balanceProduct : balanceProducts) {
            out += (balanceProduct.getConsumeCount() * balanceProduct.getProduct().getPrice());
            counts.add(balanceProduct.getConsumePeopleCount());
        }
        for (Integer i : counts) {
            count += i;
        }

        model.addAttribute("out", out);
        model.addAttribute("count", count);

        model.addAttribute("date1", dateModel.getStartDate());
        model.addAttribute("date2", dateModel.getEndDate());

        return "consume_history";
    }

    @RequestMapping("/consumeHistoryByDate")
    public String showConsumeHistoryByDatePage(Model model, @RequestParam(value = "startDate") String date1, @RequestParam(value = "endDate") String date2) throws ParseException {
        if (date1 == null || date1.isEmpty()) {
            date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (date2 == null || date2.isEmpty()) {
            date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        int in = 0;
        int out = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date1));


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date2));

        List<BalanceProduct> balanceProducts = balanceProductService.getAllIfConsumedByDate(calendar.getTime(), calendarend.getTime());

        balanceProducts.sort((o1, o2) -> o1.getBalanceDate().compareTo(o2.getBalanceDate()));
        Collections.reverse(balanceProducts);

        int count = 0;
        Set<Integer> counts = new HashSet<>();
        model.addAttribute("balanceProducts", balanceProducts);
        for (BalanceProduct balanceProduct : balanceProducts) {
            out += (balanceProduct.getConsumeCount() * balanceProduct.getProduct().getPrice());
            counts.add(balanceProduct.getConsumePeopleCount());
        }
        for (Integer i : counts) {
            count += i;
        }

        model.addAttribute("out", out);
        model.addAttribute("count", count);

        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);

        return "consume_history";
    }


    @RequestMapping(value = "/newProductForBalance")
    public ModelAndView addToBalance(Model model, @RequestParam String date,
                                     @RequestParam int consumePeopleCount, @RequestParam String startDate,
                                     @RequestParam String endDate) throws ParseException {


        if (date == null || date.isEmpty()) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        if (startDate == null || startDate.isEmpty()) {
            startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        if (endDate == null || endDate.isEmpty()) {
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        ModelAndView mav = new ModelAndView("new_product_balance");


        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));

        long page = tempBalanceService.size();
        List<Product> listProducts = service.listAll();
        listProducts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        Collections.reverse(listProducts);
        List<Product> list = new ArrayList<>();
        list.add(listProducts.get((int) page));
//        List<Product> listProducts = service.listAll();
//        listProducts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//        Collections.reverse(listProducts);
//
        mav.addObject("storageListProducts", list);
        mav.addObject("date", date);
        List<BalanceProductModel> balanceProductModels = new ArrayList<>();
        for (Product p : list) {
            BalanceProductModel balanceProductModel = new BalanceProductModel();
            balanceProductModel.setProduct(p);
            balanceProductModel.setProductId(p.getId());
            balanceProductModel.setBalanceDate(date);
            balanceProductModel.setConsumePeopleCount(consumePeopleCount);
            balanceProductModel.setConsumeStartDate(startDate);
            balanceProductModel.setConsumeEndDate(endDate);
            balanceProductModels.add(balanceProductModel);
        }
        BalanceProductModelList list1 = new BalanceProductModelList();
        list1.setBalanceProductModelList(balanceProductModels);
        mav.addObject("list", list1);

        return mav;
    }

    //    @RequestMapping(value = "/showUnit", method = RequestMethod.POST)
//    public void saveProduct(Model model, @ModelAttribute("list") ShowTotal list) {
//        System.out.println(list);
//    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(Model model, @ModelAttribute("product") ProductModel product1, @RequestParam(value = "action", required = true) String action) throws ParseException {
        Product product = new Product();
        if (product1.getByKg() != 0.0) {
            product.setUnit("Կիլոգրամ");
            product.setCount(product1.getCount() * product1.getByKg());
            product.setPrice(product1.getPrice() / product1.getByKg());
        } else {
            product.setCount(product1.getCount());
            product.setPrice(product1.getPrice());
            product.setUnit(product1.getUnit());
        }
        product.setId(product1.getId());
        product.setCompany(product1.getCompany());
        product.setName(product1.getName());
        product.setProductType(product1.getProductType());
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(product1.getDate());
        product.setDate(date);

        Product fromStorage = service.findProductByName(product.getName(), product.getUnit());

        StorageHistory storageHistory = new StorageHistory();
        storageHistory.setProductId(product.getId());
        storageHistory.setName(product.getName());
        storageHistory.setProductType(product.getProductType());
        storageHistory.setPrice(product.getPrice());
        storageHistory.setUnit(product.getUnit());
        storageHistory.setCount(product.getCount());
        storageHistory.setTotalPrice(product.getCount() * product.getPrice());
        storageHistory.setCompany(product.getCompany());
        storageHistory.setDate(product.getDate());
        storageHistory.setIn(true);
        if (fromStorage != null) {
            storageHistory.setOldCount(fromStorage.getCount());
            storageHistory.setOldPrice(fromStorage.getPrice());
            storageHistory.setProductId(product.getId());
            storageHistory.setOldDate(fromStorage.getDate());
        }
        storageHistoryService.save(storageHistory);


        if (fromStorage != null) {
            //we have this product

            if (fromStorage.getPrice() != product1.getPrice()) {
                //avg price
                double totalCount = fromStorage.getCount() + product.getCount();
                double totalPrice = Math.round((fromStorage.getPrice() * fromStorage.getCount()) + (product.getPrice() * product.getCount()));
                double avg = Math.round(totalPrice / totalCount);
                fromStorage.setDate(product.getDate());
                fromStorage.setCount(fromStorage.getCount() + product.getCount());
                fromStorage.setPrice(avg);
            } else {
                //same price
                fromStorage.setCount(fromStorage.getCount() + product.getCount());
            }
            product = fromStorage;
            product.setId(fromStorage.getId());
            storageHistory.setProductId(product.getId());
            storageHistoryService.save(storageHistory);

            service.save(product);
        } else {
            //new product
            service.save(product);
            storageHistory.setProductId(product.getId());
            storageHistoryService.save(storageHistory);

        }

        if (action.equals("Next")) {

            ProductModel newProduct = new ProductModel();
            newProduct.setCompany(product1.getCompany());
            newProduct.setDate(product1.getDate());
            model.addAttribute("product", newProduct);

            return "new_product_next";
        } else {
            return "redirect:/storage";

        }

    }

    @RequestMapping(value = "/saveService", method = RequestMethod.POST)
    public String saveProductService(Model model, @ModelAttribute("service") ServiceModel serviceModel1) throws ParseException {
        Service service = new Service();

        service.setId(serviceModel1.getId());
        service.setName(serviceModel1.getName());
        service.setPrice(serviceModel1.getPrice());

        Date dateStart = new SimpleDateFormat("yyyy-MM-dd").parse(serviceModel1.getStartDate());
        Date dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(serviceModel1.getEndDate());
        service.setStartDate(dateStart);
        service.setEndDate(dateEnd);

        serviceProductService.save(service);


        return "redirect:/service";


    }

    @RequestMapping(value = "/saveProductEdit", method = RequestMethod.POST)
    public String saveProductEdit(@ModelAttribute("product") ProductModel product1) throws ParseException {
        Product product = new Product();
        product.setId(product1.getId());
        product.setCount(product1.getCount());
        product.setCompany(product1.getCompany());
        product.setName(product1.getName());
        product.setProductType(product1.getProductType());
        product.setPrice(product1.getPrice());
        product.setUnit(product1.getUnit());
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(product1.getDate());
        product.setDate(date);

        service.save(product);

        List<StorageHistory> storageHistories = storageHistoryService.getByProduct(product1.getId());
        for (StorageHistory storageHistory : storageHistories) {
            storageHistory.setName(product.getName());
            storageHistory.setProductType(product.getProductType());
            storageHistory.setPrice(product.getPrice());
            storageHistory.setUnit(product.getUnit());
            storageHistory.setCount(product.getCount());
            storageHistory.setTotalPrice(product.getCount() * product.getPrice());
            storageHistory.setCompany(product.getCompany());
            storageHistory.setDate(product.getDate());
            storageHistory.setIn(true);
            storageHistoryService.save(storageHistory);

        }


        return "redirect:/storage";
    }

    @RequestMapping(value = "/saveBalanceProduct", method = RequestMethod.POST)
    public String saveBalanceProduct(@ModelAttribute("list") BalanceProductModelList list) throws ParseException {
        for (BalanceProductModel b : list.getBalanceProductModelList()) {
            BalanceProduct balanceProduct = new BalanceProduct();
            balanceProduct.setCount(b.getCount());
            balanceProduct.setId(b.getId());
            balanceProduct.setConsumeStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getConsumeStartDate()));
            balanceProduct.setConsumeEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getConsumeEndDate()));
            balanceProduct.setConsumePeopleCount(b.getConsumePeopleCount());
            balanceProduct.setBalanceDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getBalanceDate()));
            Calendar calendar = new GregorianCalendar();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(b.getBalanceDate());
            calendar.setTime(date);
            date = calendar.getTime();
            balanceProduct.setBalanceDate(date);


            balanceProductService.save(balanceProduct);

            Product product = service.get(b.getProductId()).orElse(null);
            double oldCount = product.getCount();
            product.setCount(b.getCount());
            service.save(product);
            balanceProduct.setProduct(product);

            double consume = oldCount - product.getCount();

            if (consume != 0) {
                StorageHistory storageHistory = new StorageHistory();
                if (balanceProduct.getStorageHistoryId() != null) {
                    storageHistory = storageHistoryService.getById(balanceProduct.getStorageHistoryId()).orElse(null);
                }
                storageHistory.setProductId(product.getId());
                storageHistory.setName(product.getName());
                storageHistory.setPrice(product.getPrice());
                storageHistory.setUnit(product.getUnit());
                storageHistory.setCount(consume);
                storageHistory.setTotalPrice(consume * product.getPrice());
                storageHistory.setCompany(product.getCompany());
                storageHistory.setDate(balanceProduct.getBalanceDate());
                storageHistory.setIn(false);
                storageHistoryService.save(storageHistory);

                balanceProduct.setStorageHistoryId(storageHistory.getId());
                balanceProduct.setConsumeCount(consume);
                balanceProductService.save(balanceProduct);
            }

        }

        return "redirect:/";

    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") String id) {
        ModelAndView mav = new ModelAndView("edit_product");
        Product product = service.get(id).orElse(null);
        mav.addObject("product", product);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") String id) {
        StorageHistory storageHistory = storageHistoryService.getById(id).orElse(null);
        Product product = service.get(storageHistory.getProductId()).orElse(null);
        List<StorageHistory> list = storageHistoryService.getByProduct(product.getId());


        if (list.size() == 1) {
            service.delete(product.getId());
        } else {
            product.setCount(storageHistory.getOldCount());
            product.setPrice(storageHistory.getOldPrice());
            product.setDate(storageHistory.getOldDate());
            service.save(product);
        }
        storageHistoryService.delete(storageHistory);
        return "redirect:/";
    }

    @RequestMapping("balance/edit/{id}")
    public ModelAndView showEditBalance(@PathVariable(name = "id") String id) {
        ModelAndView mav = new ModelAndView("edit_product_balance");
        BalanceProduct balanceProduct = balanceProductService.getById(id).orElse(null);
        if (balanceProduct != null) {
            mav.addObject("balanceProduct", balanceProduct);
        }

        return mav;
    }

    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    public ModelAndView showEditBalance(@ModelAttribute("balanceProduct") BalanceProduct balanceProduct) {
        ModelAndView mav = new ModelAndView("redirect:/");
        BalanceProduct balanceProduct1 = balanceProductService.getById(balanceProduct.getId()).orElse(null);
        double oldBalance = balanceProduct1.getCount();
        double newBalance = balanceProduct.getCount();
        double consume = balanceProduct1.getConsumeCount();
        balanceProduct1.setCount(newBalance);
        balanceProduct1.setBalanceDate(balanceProduct.getBalanceDate());
        balanceProduct1.setConsumeStartDate(balanceProduct.getConsumeStartDate());
        balanceProduct1.setConsumeEndDate(balanceProduct.getConsumeEndDate());
        balanceProduct1.setConsumePeopleCount(balanceProduct.getConsumePeopleCount());
        if (newBalance > oldBalance) {
            consume = consume - (newBalance - oldBalance);
        } else if (newBalance < oldBalance) {
            consume = consume + (oldBalance - newBalance);
        }
        balanceProduct1.setConsumeCount(consume);
        Product p = balanceProduct1.getProduct();
        p.setCount(newBalance);
        balanceProduct1.setProduct(p);

        //save product
        service.save(p);

        StorageHistory storageHistory = storageHistoryService.getById(balanceProduct1.getStorageHistoryId()).orElse(null);
        storageHistory.setCount(consume);
        storageHistory.setTotalPrice(storageHistory.getCount() * storageHistory.getPrice());

        storageHistoryService.save(storageHistory);
        balanceProductService.save(balanceProduct1);


        return mav;
    }

    @RequestMapping("/balance/delete/{id}")
    public String deleteBalance(@PathVariable(name = "id") String id) {
        BalanceProduct balanceProduct = balanceProductService.getById(id).orElse(null);

        String storageHistoryId = balanceProduct.getStorageHistoryId();
        String productId = balanceProduct.getProduct().getId();
        double deleteChanges = balanceProduct.getCount() + balanceProduct.getConsumeCount();

        Product product = service.get(productId).orElse(null);
        product.setCount(deleteChanges);
        service.save(product);
        balanceProductService.deleteById(id);
        storageHistoryService.deleteById(storageHistoryId);
        return "redirect:/";
    }

    @RequestMapping("/service/delete/{id}")
    public String deleteService(@PathVariable(name = "id") String id) {
        Service service = serviceProductService.getById(id).orElse(null);

        serviceProductService.delete(service);
        return "redirect:/service";
    }

    @RequestMapping("/service/update/{id}")
    public ModelAndView showEditService(@PathVariable(name = "id") String id) {
        ModelAndView mav = new ModelAndView("edit_product_service");
        Service service = serviceProductService.getById(id).orElse(null);
        if (service != null) {
            mav.addObject("service", service);
        }

        return mav;

    }

    @RequestMapping(value = "/nextSaveBalanceProduct", method = RequestMethod.POST)
    public ModelAndView nextSaveBalanceProduct(@ModelAttribute("list") BalanceProductModelList list) throws ParseException {
        ModelAndView mav = new ModelAndView("index");

        for (BalanceProductModel b : list.getBalanceProductModelList()) {
            TempBalanceProduct balanceProduct = new TempBalanceProduct();
            balanceProduct.setCount(b.getCount());
            balanceProduct.setId(b.getId());
            balanceProduct.setConsumeStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getConsumeStartDate()));
            balanceProduct.setConsumeEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getConsumeEndDate()));
            balanceProduct.setConsumePeopleCount(b.getConsumePeopleCount());
            balanceProduct.setBalanceDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getBalanceDate()));
            Calendar calendar = new GregorianCalendar();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(b.getBalanceDate());
            calendar.setTime(date);
            date = calendar.getTime();
            balanceProduct.setBalanceDate(date);
            tempBalanceService.save(balanceProduct);

            Product product = service.get(b.getProductId()).orElse(null);
            double oldCount = product.getCount();

            balanceProduct.setProduct(product);

            double consume = oldCount - b.getCount();

//                StorageHistory storageHistory = new StorageHistory();
//                if (balanceProduct.getStorageHistoryId() != null) {
//                    storageHistory = storageHistoryService.getById(balanceProduct.getStorageHistoryId()).orElse(null);
//                }
//
//                balanceProduct.setStorageHistoryId(storageHistory.getId());
            StorageHistory storageHistory = new StorageHistory();
//                    if (balanceProduct.getStorageHistoryId() != null) {
//                        storageHistory = storageHistoryService.getById(balanceProduct.getStorageHistoryId()).orElse(null);
//                    }

            storageHistory.setProductId(product.getId());
            storageHistory.setName(product.getName());
            storageHistory.setPrice(product.getPrice());
            storageHistory.setUnit(product.getUnit());
            storageHistory.setCount(consume);
            storageHistory.setTotalPrice(consume * product.getPrice());
            storageHistory.setCompany(product.getCompany());
            storageHistory.setDate(balanceProduct.getBalanceDate());
            storageHistory.setIn(false);
            storageHistory.setProductType(product.getProductType());
            storageHistoryService.save(storageHistory);

            balanceProduct.setStorageHistoryId(storageHistory.getId());
            balanceProduct.setConsumeCount(consume);
            tempBalanceService.save(balanceProduct);

        }
        if (tempBalanceService.size() == service.size()) {
            BalanceProduct balanceProduct = new BalanceProduct();
            for (TempBalanceProduct temp : tempBalanceService.getAll()) {
                balanceProduct.setId(null);
                balanceProduct.setProduct(temp.getProduct());
                balanceProduct.setConsumePeopleCount(temp.getConsumePeopleCount());
                balanceProduct.setConsumeEndDate(temp.getConsumeEndDate());
                balanceProduct.setConsumeStartDate(temp.getConsumeEndDate());
                balanceProduct.setCount(temp.getCount());
                balanceProduct.setStorageHistoryId(temp.getStorageHistoryId());
                balanceProduct.setConsumeCount(temp.getConsumeCount());
                balanceProduct.setBalanceDate(temp.getBalanceDate());

                balanceProductService.save(balanceProduct);
                Product product = service.get(temp.getProduct().getId()).orElse(null);
                double oldCount = product.getCount();
                product.setCount(temp.getCount());
                service.save(product);
                balanceProduct.setProduct(product);

                double consume = oldCount - product.getCount();

                StorageHistory storageHistory = new StorageHistory();
                if (balanceProduct.getStorageHistoryId() != null) {
                    storageHistory = storageHistoryService.getById(balanceProduct.getStorageHistoryId()).orElse(null);
                }

                storageHistory.setProductId(product.getId());
                storageHistory.setName(product.getName());
                storageHistory.setPrice(product.getPrice());
                storageHistory.setUnit(product.getUnit());
                storageHistory.setCount(consume);
                storageHistory.setTotalPrice(consume * product.getPrice());
                storageHistory.setCompany(product.getCompany());
                storageHistory.setDate(balanceProduct.getBalanceDate());
                storageHistory.setIn(false);
                storageHistory.setProductType(product.getProductType());
                storageHistoryService.save(storageHistory);

                balanceProduct.setStorageHistoryId(storageHistory.getId());
                balanceProduct.setConsumeCount(consume);
                balanceProductService.save(balanceProduct);


            }
            tempBalanceService.deleteAll();

        } else {
            long page = tempBalanceService.size();

//            List<Product> listProducts = service.topFive((int) page);
            List<Product> listProducts = new ArrayList<>();
            List<Product> products = service.listAll();

            products.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            Collections.reverse(products);
            listProducts.add(products.get((int) page));
            BalanceProductModel b = list.getBalanceProductModelList().get(0);
            mav = new ModelAndView("new_product_balance");

            mav.addObject("storageListProducts", listProducts);
            mav.addObject("date", b.getBalanceDate());
            List<BalanceProductModel> balanceProductModels = new ArrayList<>();
            for (Product p : listProducts) {
                BalanceProductModel balanceProductModel = new BalanceProductModel();
                balanceProductModel.setProduct(p);
                balanceProductModel.setProductId(p.getId());
                balanceProductModel.setBalanceDate(b.getBalanceDate());
                balanceProductModel.setConsumePeopleCount(b.getConsumePeopleCount());
                balanceProductModel.setConsumeStartDate(b.getConsumeStartDate());
                balanceProductModel.setConsumeEndDate(b.getConsumeEndDate());
                balanceProductModels.add(balanceProductModel);
            }
            BalanceProductModelList list1 = new BalanceProductModelList();
            list1.setBalanceProductModelList(balanceProductModels);
            mav.addObject("list", list1);

        }

        return mav;

    }

    @RequestMapping("/replace")
    public String viewReplacePage(Model model) {

        return "replace";
    }

    @RequestMapping(value = "/newReplace", method = RequestMethod.GET)
    public ModelAndView newReplace(@RequestParam String replaceDate, @RequestParam String place) throws ParseException {
//        double count = 0.0;
//        if (replaceDate == null || replaceDate.isEmpty()) {
//            replaceDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        }
        ModelAndView mav = new ModelAndView("new_replace");
        ReplaceProductModel replaceProductModel = new ReplaceProductModel();
        replaceProductModel.setReplaceDate(replaceDate);
        replaceProductModel.setPlace(place);
//
//
//        Calendar calendarend = Calendar.getInstance();
//        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(replaceDate));
//        long page = tempBalanceService.size();
//        List<Product> listProducts = service.listAll();
//        listProducts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//        Collections.reverse(listProducts);
//        List<Product> list = new ArrayList<>();
//        list.add(listProducts.get((int) page));
////        List<Product> listProducts = service.listAll();
////        listProducts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
////        Collections.reverse(listProducts);
////
//        mav.addObject("storageListProducts", list);
//        mav.addObject("replaceDate", replaceDate);
//        List<ReplaceProductModel> replaceProductModels = new ArrayList<>();
//        for (Product p : list) {
//            ReplaceProductModel replaceProductModel = new ReplaceProductModel();
//            replaceProductModel.setProduct(p);
//            replaceProductModel.setName(p.getName());
//            replaceProductModel.setProductId(p.getId());
//            replaceProductModel.setReplaceDate(replaceDate);
//            replaceProductModel.setCount(count);
//            replaceProductModel.setOldCount(p.getCount());
//            replaceProductModel.setPlace(place);
//            replaceProductModels.add(replaceProductModel);
//        }
//        ReplaceProductModelList list1 = new ReplaceProductModelList();
//        list1.setReplaceProductModelList(replaceProductModels);
        mav.addObject("replace", replaceProductModel);
        return mav;
    }

    @RequestMapping(value = "/saveReplace", method = RequestMethod.POST)
    public ModelAndView saveReplaceModel(Model model, @ModelAttribute("replace") ReplaceProductModel replaceProductModel, @RequestParam(value = "action", required = true) String action) throws ParseException {

        ModelAndView mav = new ModelAndView("new_replace");

        Product product = service.findProductByName(replaceProductModel.getName(), replaceProductModel.getUnit());
        replaceProductModel.setProductId(product.getId());
        replaceProductModel.setProduct(product);
        replaceProductModel.setOldCount(product.getCount());
        TempReplaceProduct tempReplaceProduct = new TempReplaceProduct();

        tempReplaceProduct.setName(replaceProductModel.getName());
        tempReplaceProduct.setOldCount(replaceProductModel.getOldCount());
        tempReplaceProduct.setCount(replaceProductModel.getCount());
        tempReplaceProduct.setProduct(replaceProductModel.getProduct());
        tempReplaceProduct.setPlace(replaceProductModel.getPlace());
        tempReplaceProduct.setUnit(replaceProductModel.getUnit());
        Calendar calendarend = Calendar.getInstance();
        calendarend.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(replaceProductModel.getReplaceDate()));
        tempReplaceProduct.setReplaceDate(calendarend.getTime());
        tempReplaceService.save(tempReplaceProduct);
        if (action.equals("Next")) {
            ReplaceProductModel replaceProductModelnew = new ReplaceProductModel();
            replaceProductModelnew.setReplaceDate(replaceProductModel.getReplaceDate());
            replaceProductModelnew.setPlace(replaceProductModel.getPlace());
            replaceProductModelnew.setUnit(replaceProductModel.getUnit());
            mav.addObject("replace", replaceProductModelnew);
        } else {
            ReplaceProduct replaceProduct = new ReplaceProduct();
            for (TempReplaceProduct temp : tempReplaceService.getAll()) {
                replaceProduct.setId(null);
                replaceProduct.setProduct(temp.getProduct());
                replaceProduct.setName(temp.getProduct().getName());
                replaceProduct.setCount(temp.getCount());
                replaceProduct.setOldCount(temp.getProduct().getCount());
                replaceProduct.setReplaceDate(temp.getReplaceDate());
                replaceProduct.setPlace(temp.getPlace());
                replaceProduct.setUnit(temp.getUnit());

                replaceProductService.save(replaceProduct);
                Product product1 = service.get(temp.getProduct().getId()).orElse(null);
                product1.setCount(product1.getCount() - temp.getCount());
                service.save(product1);
                replaceProduct.setProduct(product1);
                replaceProductService.save(replaceProduct);
            }
            tempReplaceService.deleteAll();
            mav = new ModelAndView("index");

        }
        return mav;
    }

//    @RequestMapping(value = "/nextReplaceBalanceProduct", method = RequestMethod.POST)
//    public ModelAndView nextSaveReplaceProduct(@ModelAttribute("list") ReplaceProductModelList list) throws ParseException {
//        ModelAndView mav = new ModelAndView("index");
//
//        double count = 0.0;
//        for (ReplaceProductModel b : list.getReplaceProductModelList()) {
//            TempReplaceProduct replaceProduct = new TempReplaceProduct();
//            replaceProduct.setCount(b.getCount());
//            replaceProduct.setId(b.getId());
//            replaceProduct.setReplaceDate(new SimpleDateFormat("yyyy-MM-dd").parse(b.getReplaceDate()));
//            replaceProduct.setPlace(b.getPlace());
//
//            Product product = service.get(b.getProductId()).orElse(null);
//
//            replaceProduct.setProduct(product);
//            replaceProduct.setName(product.getName());
//            replaceProduct.setOldCount(product.getCount());
//            tempReplaceService.save(replaceProduct);
//        }
//        if (tempReplaceService.size() == service.size()) {
//            ReplaceProduct replaceProduct = new ReplaceProduct();
//            for (TempReplaceProduct temp : tempReplaceService.getAll()) {
//                replaceProduct.setId(null);
//                replaceProduct.setProduct(temp.getProduct());
//                replaceProduct.setName(temp.getProduct().getName());
//                replaceProduct.setCount(temp.getCount());
//                replaceProduct.setOldCount(temp.getProduct().getCount());
//                replaceProduct.setReplaceDate(temp.getReplaceDate());
//                replaceProduct.setPlace(temp.getPlace());
//
//                replaceProductService.save(replaceProduct);
//                Product product = service.get(temp.getProduct().getId()).orElse(null);
//                product.setCount(product.getCount() - temp.getCount());
//                service.save(product);
//                replaceProduct.setProduct(product);
//                replaceProductService.save(replaceProduct);
//
//
//            }
//            tempReplaceService.deleteAll();
//
//        } else {
//            long page = tempBalanceService.size();
//
////            List<Product> listProducts = service.topFive((int) page);
//            List<Product> listProducts = new ArrayList<>();
//            List<Product> products = service.listAll();
//
//            products.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//            Collections.reverse(products);
//            listProducts.add(products.get((int) page));
//            ReplaceProductModel b = list.getReplaceProductModelList().get(0);
//            mav = new ModelAndView("new_replace");
//
//            mav.addObject("storageListProducts", listProducts);
//            mav.addObject("replaceDate", b.getReplaceDate());
//            List<ReplaceProductModel> replaceProductModels = new ArrayList<>();
//            for (Product p : listProducts) {
//                ReplaceProductModel replaceProductModel = new ReplaceProductModel();
//                replaceProductModel.setProduct(p);
//                replaceProductModel.setName(p.getName());
//                replaceProductModel.setProductId(p.getId());
//                replaceProductModel.setReplaceDate(b.getReplaceDate());
//                replaceProductModel.setCount(count);
//                replaceProductModel.setOldCount(p.getCount());
//                replaceProductModel.setPlace(b.getPlace());
//                replaceProductModels.add(replaceProductModel);
//            }
//            ReplaceProductModelList list1 = new ReplaceProductModelList();
//            list1.setReplaceProductModelList(replaceProductModels);
//            mav.addObject("list", list1);
//
//        }
//
//        return mav;
//
//
//    }

    @RequestMapping("/replaceHistoryByDate")
    public ModelAndView showReplaceHistoryByDatePage(Model model, @RequestParam String name, @RequestParam String startDate,@RequestParam String endDate, @RequestParam String place) throws ParseException {
        ModelAndView mav = new ModelAndView("replace_history");


        Calendar calendar = Calendar.getInstance();
        if (startDate != null && !startDate.isEmpty()) {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
        }


        Calendar calendar1 = Calendar.getInstance();

        if (endDate != null && !endDate.isEmpty()) {
            calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
        }
        List<ReplaceProduct> replaceHistoryList = replaceProductService.listAllByDateNamePlace(calendar.getTime(), calendar1.getTime(), name, place);
        replaceHistoryList.sort((o1, o2) -> o1.getReplaceDate().compareTo(o2.getReplaceDate()));
        Collections.reverse(replaceHistoryList);

        model.addAttribute("replaceHistoryList", replaceHistoryList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("place", place);
        model.addAttribute("name", name);

        return mav;

    }

    @RequestMapping("replace/delete/{id}")
    public String deleteReplaceProduct(@PathVariable(name = "id") String id) {
        ReplaceProduct replaceProduct = replaceProductService.getById(id).orElse(null);
        Product product = replaceProduct.getProduct();
        product.setCount(replaceProduct.getOldCount());
        service.save(product);
        replaceProductService.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("replace/edit/{id}")
    public ModelAndView showEditReplace(@PathVariable(name = "id") String id) {
        ModelAndView mav = new ModelAndView("edit_product_replace");
        ReplaceProduct replaceProduct = replaceProductService.getById(id).orElse(null);
        if (replaceProduct != null) {
            mav.addObject("replaceProduct", replaceProduct);
        }

        return mav;
    }
    @RequestMapping(value = "/saveEditReplace", method = RequestMethod.POST)
    public ModelAndView showEditReplace(@ModelAttribute("replaceProduct") ReplaceProduct replaceProduct) {
        double newCount = replaceProduct.getOldCount() - replaceProduct.getCount();
        Product product = replaceProduct.getProduct();
        product.setCount(newCount);
        replaceProduct.setProduct(product);
        service.save(product);
        replaceProductService.save(replaceProduct);
        return new ModelAndView("index");
    }
}