//package com.r2s.mobilestore.common.util;
//
//import com.r2s.mobilestore.data.entity.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//import org.apache.poi.ss.usermodel.*;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//public class ExcelHelper {
//    public static String TYPE = "application/vnd.ms-excel.sheet.macroenabled.12";
//    static String[] HEADERs = {"STT", "Tên công việc", "Vị trí", "Chuyên ngành", "Hình thức công việc", "Trợ cấp tối thiểu", "Trợ cấp tối đa", "Địa chỉ", "Tỉnh/TP", "Mô tả công việc", "Yêu cầu công vệc", "Chế độ phúc lợi", "Ngày đăng tuyển", "Hạn nộp"};
//    static String MAINSHEET = "Job";
//    static String VERSIONSHEET = "Version";
//
//    @Value("${r2s.excelVersion}")
//    private String excelVersion;
//
//    public static boolean hasExcelFormat(MultipartFile file) {
//        return TYPE.equals(file.getContentType());
//    }
//
//    public boolean checkFileVersion(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            //Load sheet name "Version"
//            Sheet sheet = workbook.getSheet(VERSIONSHEET);
//            Iterator<Row> rows = sheet.iterator();
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//                int cellIdx = 0;
//                while (cellsInRow.hasNext()) {
//
//                    Cell currentCell = cellsInRow.next();
//
//                    if (cellIdx == 0) {
//                        String version = currentCell.getStringCellValue();
//                        if (excelVersion.equals(version))
//                            return true;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//    }
//
//    public List<Job> excelToJob(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            //Load sheet name "Job"
//            Sheet sheet = workbook.getSheet(MAINSHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//
//            List<Job> jobs = new ArrayList<>();
//
//            int rowNumber = 0;
//            boolean flage = false;
//            while (rows.hasNext() && !flage) {
//                Row currentRow = rows.next();
//
//                // skip 10 first rows
//                if (rowNumber < 10) {
//                    rowNumber++;
//                    continue;
//                }
//                rowNumber++;
//
//                Job job = new Job();
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                int cellIdx = 0;
//                String address = "";
//                while (cellsInRow.hasNext() && !flage) {
//
//
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//                        case 0:
//                            double number = currentCell.getNumericCellValue();
//                            if (number == 0)
//                                flage = true;
//                            break;
//                        case 1:
//                            String s = currentCell.getStringCellValue();
//                            job.setName(currentCell.getStringCellValue());
//                            break;
//
//                        case 2:
//                            //Take string from cell in excel
//                            String joPoss = currentCell.getStringCellValue();
//                            //slip string to list string by ";"
//                            List<String> listStrJobPoss = Stream.of(joPoss.split(";")).collect(Collectors.toList());
//                            //create list JobPosition from list of string job position
//                            List<JobPosition> listJobPoss = new ArrayList<>();
//                            for (String jobPos : listStrJobPoss) {
//                                Position position = new Position();
//                                position.setName(jobPos);
//                                JobPosition jobPosition = new JobPosition();
//                                jobPosition.setPosition(position);
//                                listJobPoss.add(jobPosition);
//                            }
//                            //Add list JobPosition to the job
//                            job.setJobPositions(listJobPoss);
//                            break;
//
//                        case 3:
//                            //Take string from cell in excel
//                            String majorStr = currentCell.getStringCellValue();
//                            //slip string to list string by ";"
//                            List<String> listStrMajor = Stream.of(majorStr.split(";")).collect(Collectors.toList());
//                            //create list major from list of string major
//                            List<JobMajor> listMajors = new ArrayList<>();
//                            for (String majorS : listStrMajor) {
//                                Major major = new Major();
//                                major.setName(majorS);
//                                JobMajor jobMajor = new JobMajor();
//                                jobMajor.setMajor(major);
//                                listMajors.add(jobMajor);
//                            }
//                            job.setJobMajors(listMajors);
//                            break;
//
//                        case 4:
//                            //Take string from cell in excel
//                            String jobTypeStr = currentCell.getStringCellValue();
//                            //slip string to list string by ";"
//                            List<String> listStrJobtype = Stream.of(jobTypeStr.split(";")).collect(Collectors.toList());
//                            //create list jobtype from list of string jobtype
//                            List<JobSchedule> jobSchedules = new ArrayList<>();
//                            for (String jobtypeS : listStrJobtype) {
//                                Schedule schedule = new Schedule();
//                                schedule.setName(jobtypeS);
//                                JobSchedule jobSchedule = new JobSchedule();
//                                jobSchedule.setSchedule(schedule);
//                                jobSchedules.add(jobSchedule);
//                            }
//                            job.setJobSchedules(jobSchedules);
//                            break;
//
//                        case 5:
//                            job.setAmount((int) currentCell.getNumericCellValue());
//                            break;
//
//                        case 6:
//                            job.setSalaryMin((long) currentCell.getNumericCellValue());
//                            break;
//
//                        case 7:
//                            job.setSalaryMax((long) currentCell.getNumericCellValue());
//                            break;
//
//                        case 8:
//                            address = address + currentCell.getStringCellValue();
//                            break;
//
//                        case 9:
//                            address = address + ", " + currentCell.getStringCellValue();
//                            job.setLocation(address);
//                            break;
//
//                        case 10:
//                            job.setDescription(currentCell.getStringCellValue());
//                            break;
//
//                        case 11:
//                            job.setRequirement(currentCell.getStringCellValue());
//                            break;
//
//                        case 12:
//                            job.setOtherInfo(currentCell.getStringCellValue());
//                            break;
//
//                        case 13:
//                            LocalDateTime dateStart = currentCell.getLocalDateTimeCellValue();
//                            if (dateStart == null)
//                                job.setStartDate(new Date());
//                            else{
//                                Date startdate = java.sql.Timestamp.valueOf(dateStart);
//                                job.setStartDate(startdate);
//                            }
//                            break;
//
//                        case 14:
//                            LocalDateTime dateEnd = currentCell.getLocalDateTimeCellValue();
//                            job.setEndDate(java.sql.Timestamp.valueOf(dateEnd));
//                            break;
//
//                        default:
//                            break;
//                    }
//
//
//                    cellIdx++;
//                }
//                if (!flage)
//                    jobs.add(job);
//            }
//            workbook.close();
//            return jobs;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//}
