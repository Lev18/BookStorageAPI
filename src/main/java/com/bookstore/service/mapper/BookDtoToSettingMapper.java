package com.bookstore.service.mapper;

import com.bookstore.entity.Setting;
import com.bookstore.entity.Setting;
import com.bookstore.repository.SettingRepository;
import com.bookstore.service.dto.BookCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                String cleanSetting = s.replaceAll("'", "").trim();
                Setting setting = allSettingExists.get(cleanSetting);
                if (setting == null) {
                    setting = new Setting(cleanSetting);
                    newSettings.add(setting);
                    allSettingExists.put(cleanSetting, setting);
                }
                allSettings.add(setting);
            }
        }
        return allSettings;
    }
}
