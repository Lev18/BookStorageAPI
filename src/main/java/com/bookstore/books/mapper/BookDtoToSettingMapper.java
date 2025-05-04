package com.bookstore.books.mapper;

import com.bookstore.books.entity.Setting;
import com.bookstore.books.repository.SettingRepository;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookDtoToSettingMapper {
    private final SettingRepository settingRepository;
    public List<Setting> mapBookDtoToSetting(BookCsvDto bookDto, 
                                             Map<String, Setting> allSettingExists,
                                             List<Setting> newSettings) {
        List<Setting> allSettings = new ArrayList<>();
        for (String s : bookDto.getSetting()) {
            if (!s.isBlank()) {
                String cleanSetting = s.replaceAll("'", "").trim().toLowerCase();
                Setting setting = allSettingExists.computeIfAbsent(cleanSetting, key-> {
                    Setting newSetting = new Setting(cleanSetting);
                    newSettings.add(newSetting);
                    return newSetting;
                });
                allSettings.add(setting);
            }
        }
        return allSettings;
    }
}
